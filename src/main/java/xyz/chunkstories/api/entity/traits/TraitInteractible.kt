//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.input.Input

abstract class TraitInteractible(entity: Entity) : Trait(entity) {
    override val traitName = "interaction"

    abstract fun handleInteraction(entity: Entity, input: Input): Boolean
}
