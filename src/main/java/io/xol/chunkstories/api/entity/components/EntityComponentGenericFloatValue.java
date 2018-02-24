//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

/**
 * Generic class for not duplacting boring code everywhere
 * Remember: you still have to declare the actual components classes in .components files !
 */
public class EntityComponentGenericFloatValue extends EntityComponent
{
	protected float value;
	
	public EntityComponentGenericFloatValue(Entity entity, float defaultValue)
	{
		super(entity);
		this.value = defaultValue;
	}
	
	public float getValue()
	{
		return value;
	}
	
	public boolean setValue(float newValue)
	{
		this.value = newValue;
		
		this.pushComponentEveryone();
		return true;
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException
	{
		dos.writeFloat(value);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException
	{
		value = dis.readFloat();
	}

}
