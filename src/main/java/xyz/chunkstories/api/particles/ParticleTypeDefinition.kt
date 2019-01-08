//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition

class ParticleTypeDefinition(val store: Content.ParticlesTypes, name: String, properties: Map<String, String>) : Definition(name, properties) {
    fun store() = store

    /**Returns the name of the render pass we should use to render those particles.  */
    val renderPass: String = resolveProperty("renderPass", "forward")

    val shaderName: String = resolveProperty("shader", "particles")

    /** Returns null or a path to an asset.  */
    val albedoTexture: String = resolveProperty("albedoTexture", "res/textures/particle.png")

    /** Returns null or a path to an asset.  */
    val normalTexture: String? = resolveProperty("normalTexture")

    /** Returns null or a path to an asset.  */
    val materialTexture: String? = resolveProperty("materialTexture")

    /** Defaults to 1.0f  */
    val billboardSize: Float = resolveProperty("billboardSize")?.toFloatOrNull() ?: 1.0F
}
