//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.input.Input

abstract class Trait(val entity: Entity) {
    val id: Int = entity.traits.registerTrait(this)

    /** Give each trait an unique name ! If a trait has the same name as another and is registered after, it will replace it. Trait names are also used for serialization. */
    abstract val traitName: String

    open fun tick() {

    }

    open fun handleInput(input: Input): Boolean {
        return false
    }
}
