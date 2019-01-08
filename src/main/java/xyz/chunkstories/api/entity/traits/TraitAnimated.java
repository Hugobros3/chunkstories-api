//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.animation.Animator;
import xyz.chunkstories.api.entity.Entity;

public abstract class TraitAnimated extends Trait {
	public TraitAnimated(Entity entity) {
		super(entity);
	}

	public abstract Animator getAnimatedSkeleton();
}
