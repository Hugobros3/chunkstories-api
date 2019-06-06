//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles

import org.joml.Vector3d
import org.joml.Vector3f
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.graphics.structs.InterfaceBlock
import xyz.chunkstories.api.world.World

abstract class ParticleType<T: ParticleType.Particle>(val definition: ParticleTypeDefinition) {

    abstract fun new(location: Location): T

    abstract class Particle : InterfaceBlock {
        val position = Vector3d()
    }

    abstract val iterationLogic: T.(World) -> T?
}