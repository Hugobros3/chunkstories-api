package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.VoxelContext;

/** An exception about some voxel 
 * Doesn't extend ChunkException to account for the fact some exceptions come from unloaded stuff, 
 * or that some voxel contexts are actually not referencing any real world
 * */
@SuppressWarnings("serial")
public abstract class VoxelException extends WorldException {

	private final VoxelContext context;
	
	public VoxelException(VoxelContext context) {
		super(context.getWorld());
		this.context = context;
	}

	public VoxelContext getContext() {
		return context;
	}
}
