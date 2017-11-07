package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * This component is just a flag describing the existence of an entity.
 */
public final class EntityComponentExistence extends EntityComponent
{
	public EntityComponentExistence(Entity entity, EntityComponent previous)
	{
		super(entity, previous);
	}

	boolean exists = true;

	public boolean exists()
	{
		return exists;
	}

	public void destroyEntity()
	{
		exists = false;
		this.pushComponentEveryone();
	}

	@Override
	public void push(StreamTarget to, DataOutputStream dos) throws IOException
	{
		//Very important: Never pretend an entity exists when it doesn't. You can do the opposite but please don't do this.
		if (!exists)
		{
			dos.writeBoolean(false);
			return;
		}
		//In the multiplayer server scenario can pretend it doesn't so the client despawns it
		else if (to instanceof Subscriber)
		{
			if (!entity.isSubscribed(to))
			{
				//System.out.println(to + " is not in the subscribers list, telling him the entity doesn't exist anymore >:D");
				dos.writeBoolean(false);
				return;
			}
		}
		//Or just be honest
		dos.writeBoolean(true);
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException
	{
		boolean existedBefore = exists;
		boolean willExist = dis.readBoolean();
		if (!willExist && existedBefore && !(entity.getWorld() instanceof WorldMaster))
		{
			//If the entity existedBefore and we are not the world master, we remove it from our world ( == it's a despawn packet )
			entity.getWorld().removeEntity(entity);
		}
	}

}
