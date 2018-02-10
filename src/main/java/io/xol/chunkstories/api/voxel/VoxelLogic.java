package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.world.cell.FutureCell;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * A voxel that has special logic for placement and removal
 */
public interface VoxelLogic
{
	/**
	 * Called before setting a cell to this Voxel type. Previous state is assumed to be air.
	 * 
	 * @param newData The data we want to place here. You are welcome to modify it !
	 * 
	 * @throws Throw a IllegalBlockModificationException if you want to stop the modification from happening altogether.
	 */
	public default void onPlace(FutureCell newData, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
	
	/**
	 * Called before replacing a cell contaning this voxel type with air.
	 * 
	 * @param context Current data in this cell.
	 * @param cause The cause of this modification ( can be an Entity )
	 * 
	 * @throws Throw a IllegalBlockModificationException if you want to stop the modification from happening.
	 */
	public default void onRemove(ChunkCell context, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
	
	/**
	 * Called when either the metadata, block_light or sun_light values of a cell of this Voxel type is touched.
	 * 
	 * @param context The current data in this cell.
	 * @param newData The future data we want to put there
	 * @param cause The cause of this modification ( can be an Entity )
	 * 
	 * @throws IllegalBlockModificationException If we want to prevent it
	 */
	public default void onModification(ChunkCell context, FutureCell newData, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
}
