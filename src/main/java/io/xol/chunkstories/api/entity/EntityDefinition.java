package io.xol.chunkstories.api.entity;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.content.Content.EntityDefinitions;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface EntityDefinition extends Definition
{
	public String getName();
	
	//public short getId();
	
	public Entity create(Location location);
	
	public EntityDefinitions store();
}
