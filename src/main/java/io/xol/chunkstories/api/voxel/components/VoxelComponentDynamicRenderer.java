package io.xol.chunkstories.api.voxel.components;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.cell.CellComponents;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

/** 
 * This component isn't used for anything but to signal the game than this voxel
 * holds some special information
 */
public abstract class VoxelComponentDynamicRenderer extends VoxelComponent {

	public VoxelComponentDynamicRenderer(CellComponents holder) {
		super(holder);
	}
	
	public abstract VoxelDynamicRenderer getVoxelDynamicRenderer();
	
	public interface VoxelDynamicRenderer {
		public void renderVoxels(RenderingInterface renderer, IterableIterator<ChunkCell> voxelsOfThisType);
	}
}
