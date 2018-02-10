package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.voxel.components.VoxelComponentDynamicRenderer;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

public interface VoxelDynamicallyRendered {
	public VoxelComponentDynamicRenderer getDynamicRendererComponent(ChunkCell context);
}
