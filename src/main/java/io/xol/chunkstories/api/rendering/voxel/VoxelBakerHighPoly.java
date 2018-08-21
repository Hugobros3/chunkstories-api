//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.voxel;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

/** Used to create intricate models, with floating point coordinates. Consumes
 * more VRAM. */
public interface VoxelBakerHighPoly extends VoxelBakerCommon {
	/** Begins a vertex at that floating point coordinate */
	public void beginVertex(float f0, float f1, float f2);

	/** Overload for beginVertex(f0, f1, f2) */
	public void beginVertex(Vector3fc vertex);

	/** Overload for beginVertex(f0, f1, f2) */
	public void beginVertex(Vector3dc vertex);
}
