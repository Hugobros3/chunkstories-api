package io.xol.chunkstories.api.converter.mappings;

import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.world.cell.FutureCell;

/** 
 * Used by the map importer/converter. 
 */
public abstract class Mapper {
	protected final Voxel voxel;
	
	public Mapper(Voxel voxel) {
		this.voxel = voxel;
	}
	
	/** Translates the method's parameters into something in {@link io.xol.chunkstories.api.voxel.VoxelFormat}. */
	public abstract void output(int minecraftId, byte minecraftMeta, FutureCell fvc);
}