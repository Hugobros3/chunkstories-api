//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.animation;

import org.joml.Matrix4fc;

import io.xol.chunkstories.api.rendering.RenderingInterface;

/** Used for renderable meshes with a skeletal structure, only provides details
 * relevant to displaying the model, rather than intrinsic details on the
 * animation structure. */
public interface SkeletonAnimator {
	/** Used to draw the debug bone armature */
	public Matrix4fc getBoneHierarchyTransformationMatrix(String nameOfEndBone, double animationTime);

	/** Used to draw deformed mesh parts */
	public Matrix4fc getBoneHierarchyTransformationMatrixWithOffset(String nameOfEndBone, double animationTime);

	/** Used to hide body parts and/or do multipass rendering */
	public boolean shouldHideBone(RenderingInterface renderingContext, String boneName);
}
