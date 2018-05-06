//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.cell;

import java.util.Map.Entry;

import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.voxel.components.VoxelComponent;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldUser;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

import javax.annotation.Nullable;

/** Represents the various VoxelComponents that may exist in one voxel cell */
public interface CellComponents {
	public Chunk getChunk();

	public World getWorld();
	
	/** Returns the WORLD x coordinate */
	public int getX();

	/** Returns the WORLD y coordinate */
	public int getY();

	/** Returns the WORLD z coordinate */
	public int getZ();
	
	/** Peeks the cell containing those components */
	public ChunkCell cell();
	
	/** Returns a list of users that can see this cell */
	public IterableIterator<WorldUser> users();
	
	///** Removes any components this cell might have had */
	//public void erase();
	
	///** Add a new named component here */
	//public void put(String name, VoxelComponent component);
	
	@Nullable
	public VoxelComponent get(String name);

	/** Looks for a VoxelComponent and returns it's name if it is contained in this cell. */
	@Nullable
	public String name(VoxelComponent component);

	public IterableIterator<Entry<String, VoxelComponent>> all();
}
