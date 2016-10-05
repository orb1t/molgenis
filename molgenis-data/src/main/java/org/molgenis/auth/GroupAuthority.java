package org.molgenis.auth;

import org.molgenis.data.Entity;
import org.molgenis.data.meta.model.EntityMetaData;

import static org.molgenis.auth.GroupAuthorityMetaData.ID;
import static org.molgenis.auth.GroupAuthorityMetaData.GROUP;

public class GroupAuthority extends Authority
{
	public GroupAuthority(Entity entity)
	{
		super(entity);
	}

	public GroupAuthority(EntityMetaData entityMeta)
	{
		super(entityMeta);
	}

	public GroupAuthority(String id, EntityMetaData entityMeta)
	{
		super(entityMeta);
		setId(id);
	}

	public String getId()
	{
		return getString(ID);
	}

	public void setId(String id)
	{
		set(ID, id);
	}

	public Group getGroup()
	{
		return getEntity(GROUP, Group.class);
	}

	public void setGroup(Group group)
	{
		set(GROUP, group);
	}
}
