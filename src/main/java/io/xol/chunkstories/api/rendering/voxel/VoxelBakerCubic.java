//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.voxel;

/** Only able to have whole/integer vertice coordinates, saves VRAM. */
public interface VoxelBakerCubic extends VoxelBakerCommon
{
	/** Begins a vertex at those integer coordinates */
	public void beginVertex(int i0, int i1, int i2);
}
