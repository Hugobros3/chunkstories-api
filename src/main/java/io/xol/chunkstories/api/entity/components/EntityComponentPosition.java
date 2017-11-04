package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class EntityComponentPosition extends EntityComponent
{
	public EntityComponentPosition(Entity entity, Location location)
	{
		super(entity);
		this.pos = location;
	}

	//private SafeWriteLock safetyLock = new SafeWriteLock();
	private Location pos;// = new Location(entity.getWorld(), 0, 0, 0);
	private Chunk chunkWithin;
	
	public void setLocation(Location location)
	{	
		assert location != null;
		assert location.getWorld() == pos.getWorld();
		
		this.pos = location;

		checkPositionAndUpdateHolder();
		
		//Push updates to everyone subscribed to this
		//In client mode it means that the controlled entity has the server subscribed so it will update it's status to the server
		
		//In server/master mode any drastic location change is told to everyone, as setLocation() is not called when the server receives updates
		//from the controller but only when an external event changes the location.
		this.pushComponentEveryone();
	}
	
	public void setPosition(Vector3dc position)
	{
		this.pos.x = (position.x());
		this.pos.y = (position.y());
		this.pos.z = (position.z());
		
		checkPositionAndUpdateHolder();

		//Same logic as above, refactoring should be done for clarity tho
		this.pushComponentEveryone();
	}
	
	public void setPosition(double x, double y, double z)
	{
		this.pos.x = (x);
		this.pos.y = (y);
		this.pos.z = (z);
		
		checkPositionAndUpdateHolder();

		//Same logic as above, refactoring should be done for clarity tho
		this.pushComponentEveryone();
	}

	public Location getLocation()
	{
		//safetyLock.beginRead();
		Location pos = this.pos;
		//safetyLock.endRead();
		return new Location(pos.getWorld(), pos);
	}
	
	public Chunk getChunkWithin()
	{
		return chunkWithin;
	}

	@Override
	public void push(StreamTarget to, DataOutputStream dos) throws IOException
	{
		dos.writeDouble(pos.x());
		dos.writeDouble(pos.y());
		dos.writeDouble(pos.z());
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException
	{
		if(pos == null)
			pos = new Location(this.entity.getWorld(), 0, 0, 0);
		
		pos.x = (dis.readDouble());
		pos.y = (dis.readDouble());
		pos.z = (dis.readDouble());
		
		checkPositionAndUpdateHolder();
		
		//Position updates received by the server should be told to everyone but the controller
		if(entity.getWorld() instanceof WorldMaster)
			pushComponentEveryoneButController();
	}
	
	public void trySnappingToChunk()
	{
		checkPositionAndUpdateHolder();
	}
	
	/**
	 * Prevents entities from going outside the world area and updates the parentHolder reference
	 */
	protected final boolean checkPositionAndUpdateHolder()
	{
		double worldSize = entity.getWorld().getWorldSize();
		
		pos.x = pos.x() % worldSize;
		pos.z = pos.z() % worldSize;
		
		// Loop arround the world
		if (pos.x() < 0)
			pos.x = pos.x() + worldSize;
		else if(pos.x() > worldSize)
			pos.x = pos.x() % worldSize;
		
		if (pos.z() < 0)
			pos.z = pos.z() + worldSize;
		else if(pos.z() > worldSize)
			pos.z = pos.z() % worldSize;
		
		// Get local chunk co-ordinate
		int chunkX = (int) (pos.x() / (32));
		int chunkY = (int) (pos.y() / (32));
		int chunkZ = (int) (pos.z() / (32));
		
		// Bound the entity to exist within the confines of the world
		if (chunkY < 0)
			chunkY = 0;
		if (chunkY > entity.getWorld().getMaxHeight() / (32))
			chunkY = entity.getWorld().getMaxHeight() / (32);
		
		//Don't touch updates once the entity was removed
		if(!entity.exists())
			return false;
		
		//Entities not in the world should never be added to it
		if(!entity.hasSpawned())
			return false;
		
		if (chunkWithin != null && chunkWithin.getChunkX() == chunkX && chunkWithin.getChunkY() == chunkY && chunkWithin.getChunkZ() == chunkZ)
		{
			return false; // Nothing to do !
		}
		else
		{
			if(chunkWithin != null)
				chunkWithin.removeEntity(entity);
		
			chunkWithin = entity.getWorld().getChunk(chunkX, chunkY, chunkZ);
			//When the region is loaded, add this entity to it.
			if(chunkWithin != null)// && regionWithin.isDiskDataLoaded())
				chunkWithin.addEntity(entity);
			
			return true;
		}
	}
}
