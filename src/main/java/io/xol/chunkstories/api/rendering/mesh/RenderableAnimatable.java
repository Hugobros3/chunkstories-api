//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.animation.SkeletonAnimator;
import io.xol.chunkstories.api.exceptions.rendering.RenderingException;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public interface RenderableAnimatable extends RenderableMesh {

	/** Renders the mesh using animation data, abstracted from actual in-engine means of achieving the animation. Users should consult a more detailed
	 * interface to design their shaders for. */
	public void render(RenderingInterface renderingInterface, SkeletonAnimator skeleton, double animationTime ) throws RenderingException;
}
