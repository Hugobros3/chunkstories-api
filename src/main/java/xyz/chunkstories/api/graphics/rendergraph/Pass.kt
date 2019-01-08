//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import xyz.chunkstories.api.graphics.ImageInput
import xyz.chunkstories.api.graphics.Shader
import xyz.chunkstories.api.graphics.UniformInput
import xyz.chunkstories.api.graphics.systems.drawing.DrawingSystem
import org.joml.Vector4d

open class Pass {
    constructor()
    constructor(name: String) {
        this.name = name
    }

    var name: String = "undefined"
        set(value) {
            if (field == "undefined") {
                field = value
                shaderName = value
            } else throw Exception("You can't rename a pass !")
        }

    var shaderName = "invalid"

    /** The default pass is the pass where meshes are rendered unless specified otherwise. */
    var default = false
    var final = false

    val dependencies = mutableSetOf<String>()

    val declaredDrawingSystems = mutableListOf<RegisteredDrawingSystem>()

    val imageInputs = mutableListOf<ImageInput>()
    val uniformInputs = mutableListOf<UniformInput>()

    var depth = noDepthTest
    val outputs = mutableListOf<PassOutput>()

    val hooks = mutableListOf<PassHook>()

    /** Updated when the pass is actually initialized */
    lateinit var shader: Shader
}

data class RegisteredDrawingSystem(val clazz: Class<out DrawingSystem>, val init: DrawingSystem.() -> Unit)

abstract class PassHook(internal val pass: Pass) : () -> Unit

class DepthTestingConfiguration {
    /** Disables depth testing altogether */
    var enabled = true

    /** Should we clear the buffer before proceeding ? */
    var clear = false

    /** Disable to stop the zBuffer from updating */
    var write = true

    /** If enabled is set to true this has to be configured. */
    lateinit var depthBuffer: String

    var mode: DepthTestMode = DepthTestMode.LESS_OR_EQUAL

    enum class DepthTestMode {
        GREATER, GREATER_OR_EQUAL, EQUAL, LESS_OR_EQUAL, LESS, ALWAYS
    }
}

val noDepthTest = DepthTestingConfiguration().apply { enabled = false }

/** Represents one output to a pass, includes some configuration */
class PassOutput {
    lateinit var name: String

    /** If the output buffer has a different name than the shader output, supply it here */
    var outputBuffer: String? = null

    /** Should we use blending while outputing to this ? */
    var blending: BlendMode = BlendMode.ALPHA_TEST

    /** Should we clear the buffer before proceeding ? */
    var clear = false
    var clearColor = Vector4d(0.0)

    /** Should we copy another buffer to this one before proceeding ? */
    var copy: String? = null

    enum class BlendMode {
        OVERWRITE,
        ALPHA_TEST,
        MIX,
        ADD,
        PREMULTIPLIED_ALPHA
    }
}