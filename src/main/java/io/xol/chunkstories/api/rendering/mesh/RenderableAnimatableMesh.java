//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.animation.SkeletonAnimator;
import io.xol.chunkstories.api.exceptions.rendering.RenderingException;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public interface RenderableAnimatableMesh extends RenderableMesh {

	/**
	 * Renders the mesh geometry, skinned by a {@link SkeletonAnimator} interpolated
	 * from animationTime.
	 */
	public void render(RenderingInterface renderer, SkeletonAnimator skeleton, double animationTime)
			throws RenderingException;

	/**
	 * Renders the mesh geometry, skinned by a {@link SkeletonAnimator} interpolated
	 * from animationTime, and using the materials and textures defined in the mesh
	 * file, if present
	 */
	public void renderUsingMaterials(RenderingInterface renderer, SkeletonAnimator skeleton, double animationTime)
			throws RenderingException;
}
