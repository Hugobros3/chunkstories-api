package io.xol.chunkstories.api.world.chunk;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.voxel.components.VoxelComponents;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.World.WorldVoxelContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface Chunk
{
	public World getWorld();
	
	public Region getRegion();
	
	public ChunkHolder holder();

	public int getChunkX();
	
	public int getChunkY();
	
	public int getChunkZ();
	
	/**
	 * Get the data contained in this chunk as full 32-bit data format ( see {@link VoxelFormat})
	 * The coordinates are internally modified to map to the chunk, meaning you can access it both with world coordinates or 0-31 in-chunk coordinates
	 * Just don't give it negatives
	 * @return the data contained in this chunk as full 32-bit data format ( see {@link VoxelFormat})
	 * @throws WorldException if it couldn't peek the world at the specified location for some reason
	 */
	public ChunkVoxelContext peek(int x, int y, int z);
	
	/** Convenient overload of peek() to take a Vector3dc derivative ( ie: a Location object ) */
	public ChunkVoxelContext peek(Vector3dc location);
	
	/** 
	 * Alternative to peek() that does not create any VoxelContext object<br/>
	 * <b>Does not throw exceptions</b>, instead safely returns zero upon failure.
	 * */
	public int peekSimple(int x, int y, int z);

	/**
	 * Sets the data for this block
	 * Takes a full 32-bit data format ( see {@link VoxelFormat})
	 * It will also trigger lightning and such updates
	 * @param data The raw block data, see {@link VoxelFormat}
	 * @throws WorldException if it couldn't poke the world at the specified location for some reason
	 */
	public ChunkVoxelContext poke(int x, int y, int z, int newVoxelData, WorldModificationCause cause) throws WorldException;
	
	/** 
	 * Does the same as {@link #poke(x,y,z,d)} but does not trigger any updates.
	 */
	public ChunkVoxelContext pokeSilently(int x, int y, int z, int newVoxelData) throws WorldException;

	/** 
	 * Does the same as {@link #poke(x,y,z,d)} but without creating any VoxelContext object<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently.
	 * <b>Does not take a cause argument.</b>, instead use the slower poke() method
	 */
	public void pokeSimple(int x, int y, int z, int newVoxelData);
	
	/** 
	 * Does the same as {@link #poke(x,y,z,d)} but without creating any VoxelContext object or triggering any updates<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently.
	 */
	public void pokeSimpleSilently(int x, int y, int z, int newVoxelData);

	/**
	 * Recomputes and propagates all lights within the chunk
	 * @param considerAdjacentChunks If set to true, the adjacent faces of the 6 adjacents chunks's data will be took in charge
	 */
	//public void computeVoxelLightning(boolean considerAdjacentChunks);
	
	//public boolean needsLightningUpdates();
	
	//public void markInNeedForLightningUpdate();
	
	/** Returns the interface responsible of updating the voxel light of this chunk */
	public ChunkLightUpdater lightBaker();
	
	public boolean isAirChunk();

	/** Obtains the EntityVoxel saved at the given location */
	public VoxelComponents components(int worldX, int worldY, int worldZ);
	
	/** Add some entity to the chunk's confines */
	public void addEntity(Entity entity);
	
	/** Remove some entity from the chunk's confines */
	public void removeEntity(Entity entity);
	
	public IterableIterator<Entity> getEntitiesWithinChunk();
	
	public interface ChunkVoxelContext extends WorldVoxelContext {
		public Chunk getChunk();
		
		public VoxelComponents components();
	}

	public void destroy();

}