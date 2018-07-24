//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.serializable.TraitSerializable;
import io.xol.chunkstories.api.util.Generalized;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

/**
 * Generic class for not duplacting boring code everywhere
 * Remember: you still have to declare the actual components classes in .components files !
 */
@Generalized
public abstract class TraitSerializableInt extends TraitSerializable
{
	protected int value;
	
	public TraitSerializableInt(Entity entity, int defaultValue)
	{
		super(entity);
		this.value = defaultValue;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public boolean setValue(int newValue)
	{
		this.value = newValue;
		
		this.pushComponentEveryone();
		return true;
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException
	{
		dos.writeInt(value);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException
	{
		value = dis.readInt();
	}

}
