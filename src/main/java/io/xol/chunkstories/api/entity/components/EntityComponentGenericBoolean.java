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

public abstract class EntityComponentGenericBoolean extends EntityComponent
{
	private boolean value = false;

	public boolean get()
	{
		return value;
	}

	public void set(boolean newValue)
	{
		if (this.value != newValue)
		{
			this.value = newValue;
			this.pushComponentEveryone();
		}
	}

	public EntityComponentGenericBoolean(Entity entity, EntityComponent previous)
	{
		super(entity, previous);
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException
	{
		dos.writeBoolean(value);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException
	{
		value = dis.readBoolean();
	}

}
