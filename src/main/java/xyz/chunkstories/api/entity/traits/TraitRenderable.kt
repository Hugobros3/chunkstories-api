//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler

/**

 */
abstract class TraitRenderable<T : Entity>(entity: T) : Trait(entity) {
    abstract fun buildRepresentation(representationsGobbler: RepresentationsGobbler)
}
