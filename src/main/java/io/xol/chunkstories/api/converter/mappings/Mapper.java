//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.converter.mappings;

import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.world.cell.FutureCell;

/** Is used to write out the corresponding voxel type to a Minecraft block */
public abstract class Mapper {
	protected final Voxel voxel;

	public Mapper(Voxel voxel) {
		this.voxel = voxel;
	}

	/** Translates the method's parameters into something in
	 * {@link io.xol.chunkstories.api.voxel.VoxelFormat}. */
	public abstract void output(int minecraftId, byte minecraftMeta, FutureCell fvc);
}