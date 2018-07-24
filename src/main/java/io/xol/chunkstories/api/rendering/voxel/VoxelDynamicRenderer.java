//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.voxel;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

/**
 * For voxels that want a more complex rendering to them, at the cost of
 * performance
 */
public interface VoxelDynamicRenderer extends VoxelRenderer {

	/**
	 * By default, dynamic renderers don't have a static component to them. This
	 * isn't an obligation in any way, just a soft assumption so you don't have to
	 * have an empty method in your code
	 */
	public default int bakeInto(ChunkRenderer chunkRenderer, ChunkRenderContext bakingContext, Chunk chunk,
			CellData voxelInformations) {
		return 0; // we bake zero triangles
	}

	public void renderVoxels(RenderingInterface renderer, IterableIterator<ChunkCell> voxelsOfThisType);
}