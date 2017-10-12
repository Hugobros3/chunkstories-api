package io.xol.chunkstories.api.world.chunk;

import java.util.Iterator;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * A region is 8x8x8 chunks and contains entities
 */
public interface Region
{
	public int getRegionX();
	
	public int getRegionY();
	
	public int getRegionZ();

	public Iterator<WorldUser> getChunkUsers();

	boolean registerUser(WorldUser user);

	boolean unregisterUser(WorldUser user);
	
	/**
	 * @return An iterator over each entity within this region
	 */
	Iterator<Entity> getEntitiesWithinRegion();
	
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

	public World getWorld();
}