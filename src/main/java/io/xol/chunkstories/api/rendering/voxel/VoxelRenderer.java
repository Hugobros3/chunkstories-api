//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.voxel;

import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.chunk.Chunk;

/** Implement this interface to make a custom Voxel renderer for your custom
 * Voxels */
public interface VoxelRenderer {
	public int bakeInto(ChunkRenderer chunkRenderer, ChunkRenderContext bakingContext, Chunk chunk, CellData voxelInformations);
}