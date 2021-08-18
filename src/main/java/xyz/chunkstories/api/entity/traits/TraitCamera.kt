//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.structs.Camera

abstract class TraitCamera(entity: Entity) : Trait(entity) {
    override val traitName: String = "camera"

    abstract val camera: Camera
}