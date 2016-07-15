package org.molgenis.data.cache.l2;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityKey;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.Repository;
import org.molgenis.data.transaction.DefaultMolgenisTransactionListener;
import org.molgenis.data.transaction.MolgenisTransactionManager;
import org.molgenis.data.transaction.TransactionInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

/**
 * In-memory cache of entities read from cacheable repositories.
 */
@Service
public class L2Cache extends DefaultMolgenisTransactionListener
{
	public static final Logger LOG = LoggerFactory.getLogger(L2Cache.class);
	private static final int MAX_CACHE_SIZE_PER_ENTITY = 1000;
	/**
	 * maps entity name to the loading cache with Object key and Optional dehydrated entity value
	 */
	private final ConcurrentMap<String, LoadingCache<Object, Optional<Entity>>> caches;
	private final TransactionInformation transactionInformation;

	@Autowired
	public L2Cache(MolgenisTransactionManager molgenisTransactionManager, TransactionInformation transactionInformation)
	{
		this.transactionInformation = requireNonNull(transactionInformation);
		caches = Maps.newConcurrentMap();
		requireNonNull(molgenisTransactionManager).addTransactionListener(this);
	}

	@Override
	public void afterCommitTransaction(String transactionId)
	{
		//TODO: trace logging + unit test this
		transactionInformation.getDirtyRepositories().forEach(caches::remove);
		transactionInformation.getDirtyEntities().forEach(this::evict);
	}

	private void evict(EntityKey entityKey)
	{
		LoadingCache<Object, Optional<Entity>> cache = caches.get(entityKey.getEntityName());
		if (cache != null)
		{
			cache.invalidate(entityKey.getId());
		}
	}

	/**
	 * Retrieves an entity from the cache or the underlying repository.
	 *
	 * @param repository the underlying repository
	 * @param id         the ID of the entity to retrieve
	 * @return the retrieved Entity, or null if the entity is not present.
	 * @throws com.google.common.util.concurrent.UncheckedExecutionException if the repository throws an error when
	 *                                                                       loading the entity
	 */
	public Entity get(Repository<Entity> repository, Object id)
	{
		LoadingCache<Object, Optional<Entity>> cache = getEntityCache(repository);
		return cache.getUnchecked(id.toString()).orElse(null);
	}

	/**
	 * Retrieves a list of entities from the cache. If the cache doesn't yet exist, will create the cache.
	 *
	 * @param repository the underlying repository, used to create the cache loader or to retrieve the existing cache
	 * @param ids        {@link Iterable} of the ids of the entities to retrieve
	 * @return List containing the retrieved entities
	 * @throws RuntimeException if the cache failed to load the
	 */
	public List<Entity> getBatch(Repository<Entity> repository, Iterable<Object> ids)
	{
		try
		{
			return getEntityCache(repository).getAll(ids).values().stream().filter(Optional::isPresent)
					.map(Optional::get).collect(Collectors.toList());
		}
		catch (ExecutionException exception)
		{
			// rethrow unchecked
			if (exception.getCause() != null && exception.getCause() instanceof RuntimeException)
			{
				throw (RuntimeException) exception.getCause();
			}
			throw new MolgenisDataException(exception);
		}
	}

	/**
	 * Logs cumulative cache statistics for all known caches.
	 */
	@Scheduled(fixedRate = 60000)
	public void logStatistics()
	{
		//TODO: do we want to log diff with last log instead?
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cache stats:");
			for (Map.Entry<String, LoadingCache<Object, Optional<Entity>>> cacheEntry : caches.entrySet())
			{
				LOG.debug("{}:{}", cacheEntry.getKey(), cacheEntry.getValue().stats());
			}
		}
	}

	/**
	 * Gets the existing entity cache for a {@link Repository} or creates a new one if no cache exists yet.
	 *
	 * @param repository the Repository used to create a new cache if none found, otherwise only the name of the
	 *                   repository is used to look up the existing cache
	 * @return the LoadingCache for the repository
	 */
	private LoadingCache<Object, Optional<Entity>> getEntityCache(Repository<Entity> repository)
	{
		String name = repository.getName();
		if (!caches.containsKey(name))
		{
			caches.putIfAbsent(name, createEntityCache(repository));
		}
		return caches.get(name);
	}

	/**
	 * Creates a new Entity cache
	 *
	 * @param repository the {@link Repository} to load the entities from
	 * @return newly created LoadingCache
	 */
	private LoadingCache<Object, Optional<Entity>> createEntityCache(Repository<Entity> repository)
	{
		return newBuilder().recordStats().maximumSize(MAX_CACHE_SIZE_PER_ENTITY).expireAfterAccess(10, MINUTES)
				.build(createCacheLoader(repository));
	}

	/**
	 * Creates a CacheLoader that loads entities from the repository and dehydrates them.
	 *
	 * @param repository the Repository to load the entities from
	 * @return the {@link CacheLoader}
	 */
	private CacheLoader<Object, Optional<Entity>> createCacheLoader(final Repository<Entity> repository)
	{
		return new CacheLoader<Object, Optional<Entity>>()
		{
			/**
			 * Loads a single entity from the repository.
			 * @param id ID value of the entity to retrieve
			 * @return dehydrated entity or empty if the entity was not present in the repository
			 */
			@Override
			public Optional<Entity> load(Object id)
			{
				return Optional.ofNullable(repository.findOneById(id));
			}

			/**
			 * Loads multiple entities from the repository.
			 * @param ids Iterable of String representations of the ID values
			 * @return Map mapping id to loaded entity, or to empty optional if the entity was not present in the repository
			 */
			@Override
			public Map<Object, Optional<Entity>> loadAll(Iterable<? extends Object> ids)
			{
				Stream<Object> typedIds = stream(ids.spliterator(), false).map(id -> id);
				Map<Object, Optional<Entity>> result = repository.findAll(typedIds)
						.collect(toMap(Entity::getIdValue, Optional::of));
				for (Object key : ids)
				{
					// cache the absence of these entities in the backend as empty values
					result.putIfAbsent(key, empty());
				}
				return result;
			}
		};
	}
}
