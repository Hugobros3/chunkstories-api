//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asFloat
import xyz.chunkstories.api.content.json.asString

class ParticleTypeDefinition(val store: Content.ParticlesTypes, name: String, properties: Json.Dict) : Definition(name, properties) {
    fun store() = store

    /**Returns the name of the render pass we should use to render those particles.  */
    val renderPass: String = this["renderPass"].asString ?: "forward"// resolveProperty("renderPass", "forward")

    val shaderName: String = this["shader"].asString ?: "particles"// resolveProperty("shader", "particles")

    /** Returns null or a path to an asset.  */
    val albedoTexture: String = this["albedoTexture"].asString ?: "res/textures/particle.png"// resolveProperty("albedoTexture", "res/textures/particle.png")

    /** Returns null or a path to an asset.  */
    val normalTexture: String? = this["normalTexture"].asString // resolveProperty("normalTexture")

    /** Returns null or a path to an asset.  */
    val materialTexture: String? = this["materialTexture"].asString // resolveProperty("materialTexture")

    /** Defaults to 1.0f  */
    val billboardSize: Float = this["billboardSize"].asFloat ?: 1.0F
}
