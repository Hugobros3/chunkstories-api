//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.region;

import java.util.Iterator;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldUser;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.ChunkHolder;

/**
 * A region is 8x8x8 chunks and contains entities
 */
public interface Region {

	public World getWorld();
	
	public int getRegionX();
	
	public int getRegionY();
	
	public int getRegionZ();

	public Iterator<WorldUser> getUsers();

	boolean registerUser(WorldUser user);

	boolean unregisterUser(WorldUser user);
	
	/**
	 * @return An iterator over each entity within this region
	 */
	public Iterator<Entity> getEntitiesWithinRegion();
	
	public int getNumberOfLoadedChunks();

	public boolean isDiskDataLoaded();
	
	public ChunkHolder getChunkHolder(int chunkX, int chunkY, int chunkZ);
	
	public Chunk getChunk(int chunkX, int chunkY, int chunkZ);

	public boolean isChunkLoaded(int chunkX, int chunkY, int chunkZ);

	public boolean isUnused();

	/** Will be traversable once the file representing the region at the time of calling this is done writing. */
	public Fence save();

	/** Same as above, but unloads first */
	public Fence unloadAndSave();
}