//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles

import org.joml.Vector3dc

interface ParticlesManager {
    //fun spawnParticleAtPosition(particleTypeName: String, position: Vector3dc)
    //fun spawnParticleAtPositionWithVelocity(particleTypeName: String, position: Vector3dc, velocity: Vector3dc)
    fun <T: ParticleType.Particle> spawnParticle(typeName: String, init: T.() -> Unit)

    fun <T: ParticleType.Particle> spawnParticle(type: ParticleTypeDefinition, init: T.() -> Unit)
}
