package io.xol.chunkstories.api.voxel.components;

import java.util.Map.Entry;

import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;

/** Represents the various VoxelComponents that may exist in one voxel cell */
public interface VoxelComponents {
	public Chunk getChunk();

	public World getWorld();
	
	/** Returns the WORLD x coordinate */
	public int getX();

	/** Returns the WORLD y coordinate */
	public int getY();

	/** Returns the WORLD z coordinate */
	public int getZ();
	
	/** Removes any components this cell might have had */
	public void erase();
	
	/** Add a new named component here */
	public void put(String name, VoxelComponent component);
	
	public VoxelComponent get(String name);

	/** Return the name of this component if it is within the components of this voxelComponents */
	public String name(VoxelComponent component);

	public IterableIterator<Entry<String, VoxelComponent>> all();
}
