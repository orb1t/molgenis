package org.molgenis.elasticsearch.request;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.molgenis.data.EntityMetaData;
import org.molgenis.data.Query;
import org.molgenis.elasticsearch.index.MappingsBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * Adds Sort to the SearchRequestBuilder object.
 * 
 * @author erwin
 * 
 */
public class SortGenerator implements QueryPartGenerator
{

	@Override
	public void generate(SearchRequestBuilder searchRequestBuilder, Query query, EntityMetaData entityMetaData)
	{
		if (query.getSort() != null)
		{
			for (Sort.Order sort : query.getSort())
			{
				if (sort.getProperty() == null)
				{
					throw new IllegalArgumentException(
							"Missing property for Sorting, for sorting property should be set to the fieldname where to sort on");
				}
				if (sort.getDirection() == null)
				{
					throw new IllegalArgumentException("Missing sort direction");
				}

				String sortField = sort.getProperty() + '.' + MappingsBuilder.FIELD_NOT_ANALYZED;
				SortOrder sortOrder = sort.getDirection() == Direction.ASC ? SortOrder.ASC : SortOrder.DESC;
				searchRequestBuilder.addSort(sortField, sortOrder);
			}
		}
	}
}
