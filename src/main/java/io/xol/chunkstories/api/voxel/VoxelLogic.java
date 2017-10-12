package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkVoxelContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * A voxel that has special logic for placement and removal
 */
public interface VoxelLogic
{
	/**
	 * Called when a voxel implementing this interface is placed
	 * @param voxelData The intended data to place at this location
	 * @param entity If placed by an entity
	 * 
	 * @throws IllegalBlockModificationException If we want to prevent it
	 * 
	 * @return The modified data to actually place there
	 */
	public int onPlace(ChunkVoxelContext context, int voxelData, WorldModificationCause cause) throws WorldException;
	
	/**
	 * Called when a voxel implementing this interface is removed
	 * @param voxelData Complete data of the voxel being removed
	 * @param entity If removed by an entity
	 * 
	 * @throws IllegalBlockModificationException If we want to prevent it
	 */
	public void onRemove(ChunkVoxelContext context, int voxelData, WorldModificationCause cause) throws WorldException;
	
	/**
	 * Called when a voxel implementing this interface is changed ( but the voxel type remains the same )
	 * @param voxelData The old data being replaced
	 * @param voxelData Complete new data being applied
	 * @param entity If modified by an entity
	 * 
	 * @throws IllegalBlockModificationException If we want to prevent it
	 * 
	 * @return The modified data to actually place there
	 */
	public int onModification(ChunkVoxelContext context, int voxelData, WorldModificationCause cause) throws WorldException;
}
