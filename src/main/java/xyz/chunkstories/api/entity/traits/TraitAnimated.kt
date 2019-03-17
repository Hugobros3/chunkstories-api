//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.animation.Animator
import xyz.chunkstories.api.entity.Entity

abstract class TraitAnimated(entity: Entity) : Trait(entity) {

    abstract val animatedSkeleton: Animator
}
