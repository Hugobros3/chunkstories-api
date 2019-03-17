//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity

abstract class Trait(val entity: Entity) {
    val id: Int

    init {
        id = entity.traits.registerTrait(this)
    }

    fun id(): Int {
        return id
    }
}
