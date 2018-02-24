//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.animation.SkeletonAnimator;
import io.xol.chunkstories.api.entity.Entity;

public interface EntityAnimated extends Entity
{
	public SkeletonAnimator getAnimatedSkeleton();
}
