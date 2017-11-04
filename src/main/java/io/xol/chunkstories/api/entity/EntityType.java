package io.xol.chunkstories.api.entity;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.NamedWithProperties;
import io.xol.chunkstories.api.content.Content.EntityTypes;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface EntityType extends NamedWithProperties
{
	public String getName();
	
	public short getId();
	
	public Entity create(Location location);
	
	public EntityTypes store();
}
