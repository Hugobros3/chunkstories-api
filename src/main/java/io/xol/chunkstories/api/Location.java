package io.xol.chunkstories.api;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class Location extends Vector3d
{
	World world;
	
	public Location(World world, double x, double y, double z)
	{
		super(x, y, z);
		this.world = world;
	}
	
	public Location(World world, Vector3dc position)
	{
		this(world, position.x(), position.y(), position.z());
	}

	public Location(Location location)
	{
		this.world = location.getWorld();
		this.x = (location.x);
		this.y = (location.y);
		this.z = (location.z);
	}
	
	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public int getVoxelDataAtLocation()
	{
		return world.getVoxelData(this);
	}

	public void setVoxelDataAtLocation(int voxelData)
	{
		world.setVoxelData(this, voxelData);
	}
}
