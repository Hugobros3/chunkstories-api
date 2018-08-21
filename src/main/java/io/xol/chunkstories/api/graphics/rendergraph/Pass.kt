package io.xol.chunkstories.api.graphics.rendergraph

import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import io.xol.chunkstories.api.rendering.shader.Shader

class Pass {
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

    val dependencies = mutableSetOf<String>()

    val imageInputs = mutableListOf<ImageInput>()
    val uniformInputs = mutableListOf<UniformInput>()

    var depth = noDepthTest
    val outputs = mutableListOf<PassOutput>()

    val hooks = mutableListOf<PassHook>()

    /** Updated when the pass is actually initialized */
    lateinit var shader: Shader
}

abstract class PassHook(internal val pass: Pass) : () -> Unit

class DepthTestingConfiguration {
    /** Disables depth testing altogether */
    var enabled = true

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
    lateinit var name : String

    /** If the output buffer has a different name than the shader output, supply it here */
    lateinit var outputBuffer : String

    /** Should we use blending while outputing to this ? */
    var blending: BlendMode = BlendMode.ALPHA_TEST

    /** Should we clear the buffer before proceeding ? */
    var clear = false

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

class UniformInput {
    lateinit var name: String
    lateinit var data: InterfaceBlock
}

class ImageInput {
    /** Name of the input this will bind to */
    lateinit var name: String

    /** Name of the source RenderBuffer or a path to an asset.
     * For binding dynamically created resources, please use a PassHook. */
    lateinit var source: String

    // General state fluff
    var samplingMode = SamplingMode.NEAREST
    var mipmapping = false
    var wrapping = false

    enum class SamplingMode {
        LINEAR,
        NEAREST
    }
}