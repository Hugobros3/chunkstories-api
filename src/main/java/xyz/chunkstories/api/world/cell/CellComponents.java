//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell;

import java.util.Map.Entry;

import xyz.chunkstories.api.util.IterableIterator;
import xyz.chunkstories.api.voxel.components.VoxelComponent;
import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.WorldUser;
import xyz.chunkstories.api.world.chunk.Chunk;
import xyz.chunkstories.api.world.chunk.Chunk.ChunkCell;

import javax.annotation.Nullable;

/** Represents the various VoxelComponents that may exist in one voxel
 * getCell */
public interface CellComponents {
	public Chunk getChunk();

	public World getWorld();

	/** Returns the WORLD x coordinate */
	public int getX();

	/** Returns the WORLD y coordinate */
	public int getY();

	/** Returns the WORLD z coordinate */
	public int getZ();

	/** Peeks the getCell containing those components */
	public ChunkCell getCell();

	/// ** Returns a list of users that can see this getCell */
	// public IterableIterator<WorldUser> users();

	@Nullable
	public VoxelComponent getVoxelComponent(String name);

	/** Looks for a VoxelComponent and returns it's name if it is contained in this
	 * getCell. */
	@Nullable
	public String getRegisteredComponentName(VoxelComponent component);

	public IterableIterator<Entry<String, VoxelComponent>> getAllVoxelComponents();
}
