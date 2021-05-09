//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asFloat
import xyz.chunkstories.api.content.json.asString

class ParticleTypeDefinition(val store: Content.ParticlesTypes, val name: String, val properties: Json.Dict) {
    fun store() = store

    /**Returns the name of the render pass we should use to render those particles.  */
    val renderPass: String = properties["renderPass"].asString ?: "forward"// resolveProperty("renderPass", "forward")

    val shaderName: String = properties["shader"].asString ?: "particles"// resolveProperty("shader", "particles")

    /** Returns null or a path to an asset.  */
    val albedoTexture: String = properties["albedoTexture"].asString ?: "res/textures/particle.png"// resolveProperty("albedoTexture", "res/textures/particle.png")

    /** Returns null or a path to an asset.  */
    val normalTexture: String? = properties["normalTexture"].asString // resolveProperty("normalTexture")

    /** Returns null or a path to an asset.  */
    val materialTexture: String? = properties["materialTexture"].asString // resolveProperty("materialTexture")

    /** Defaults to 1.0f  */
    val billboardSize: Float = properties["billboardSize"].asFloat ?: 1.0F
}
