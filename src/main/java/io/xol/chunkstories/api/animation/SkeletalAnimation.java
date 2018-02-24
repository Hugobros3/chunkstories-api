//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.animation;

import org.joml.Matrix4fc;

/** Describes a full animation, loaded from Content.AnimationsLibrary */
public interface SkeletalAnimation extends SkeletonAnimator {

	/** How is that bone offset to the center of the model */
	public Matrix4fc getOffsetMatrix(String boneName);
	
	public SkeletonBone getBone(String boneName);
	
	public interface SkeletonBone {
		
		/** Return the offset of this bone to the animation origin bone */
		public Matrix4fc getOffsetMatrix();

		/** The transformation matrix for this bone alone */
		public Matrix4fc getTransformationMatrix(double animationTime);
		
		/** Boneless Bones */
		public SkeletonBone getParent();

		/** Bameless Bones */
		public String getName();
	}
}
