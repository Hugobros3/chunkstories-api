package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.voxel.VoxelFormat;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Extends the notion of voxel context to add in the editability */
public interface EditableVoxelContext extends VoxelContext {
	
	/**
	 * Sets the data for this block
	 * Takes a full 32-bit data format ( see {@link VoxelFormat})
	 * It will also trigger lightning and such updates
	 * @param data The raw block data, see {@link VoxelFormat}
	 * @param cause You can specify some cause for this change. Null is accepted.
	 */
	public EditableVoxelContext poke(int newVoxelData, WorldModificationCause cause) throws WorldException;
	
	/** 
	 * Does the same as {@link poke()} but does not trigger any updates<br/>
	 * Does not take a cause since this modification will not be noticed by anything
	 */
	public EditableVoxelContext pokeSilently(int newVoxelData) throws WorldException;
	
	/** 
	 * Does the same as {@link #poke(x,y,z,d)} but without creating any VoxelContext object<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently.
	 * <b>Does not take a cause argument.</b>, instead use the slower poke() method
	 */
	public void pokeSimple(int newVoxelData);
	
	/** 
	 * Does the same as {@link #poke(x,y,z,d)} but without creating any VoxelContext object or triggering any updates<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently.
	 */
	public void pokeSimpleSilently(int newVoxelData);
}
