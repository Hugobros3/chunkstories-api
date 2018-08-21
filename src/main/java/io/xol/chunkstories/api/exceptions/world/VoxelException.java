//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.cell.CellData;

/** An exception about some voxel Doesn't extend ChunkException to account for
 * the fact some exceptions come from unloaded stuff, or that some voxel
 * contexts are actually not referencing any real world */
@SuppressWarnings("serial")
public abstract class VoxelException extends WorldException {

	private final CellData context;

	public VoxelException(CellData context) {
		super(context.getWorld());
		this.context = context;
	}

	public CellData getContext() {
		return context;
	}
}
