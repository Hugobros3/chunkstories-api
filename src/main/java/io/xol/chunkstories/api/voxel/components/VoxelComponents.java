package io.xol.chunkstories.api.voxel.components;

import java.util.Map.Entry;

import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.chunk.Chunk;

public interface VoxelComponents {
	public Chunk getChunk();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
	public void erase();
	
	public void put(String name, VoxelComponent component);
	
	public VoxelComponent get(String name);

	public IterableIterator<Entry<String, VoxelComponent>> all();
}
