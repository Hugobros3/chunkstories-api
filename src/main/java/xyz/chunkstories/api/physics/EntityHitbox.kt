//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitAnimated

data class EntityHitbox(internal val entity: Entity, val box: Box, val name: String) {
    // Resolved once and for all
    internal val animationTrait: TraitAnimated? by lazy { entity.traits[TraitAnimated::class.java] }

    override fun toString(): String {
        return "EntityHitbox(entity=$entity, box=$box, name='$name')"
    }
}