//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import org.joml.Vector4d
import xyz.chunkstories.api.graphics.ImageInput

class PassDeclaration {
    lateinit var name: String

    val passDependencies = mutableListOf<String>()
    fun dependsOn(vararg pass: String) = passDependencies.addAll(pass)

    var depthTestingConfiguration : DepthTestingConfiguration = noDepthTest
    fun depth(dslCode: DepthTestingConfiguration.() -> Unit) {
        depthTestingConfiguration = DepthTestingConfiguration().apply(dslCode)
    }

    lateinit var outputs: PassOutputsDeclaration
    fun outputs(dslCode: PassOutputsDeclaration.() -> Unit) {
        outputs = PassOutputsDeclaration().also(dslCode)
    }

    var draws: DrawsDeclarations? = null
    fun draws(dslCode : DrawsDeclarations.() -> Unit) {
        draws = DrawsDeclarations().apply(dslCode)
    }

    var inputs: PassInputsDeclarations? = null
    fun inputs(dslCode: PassInputsDeclarations.() -> Unit) {
        inputs = PassInputsDeclarations().also(dslCode)
    }
}

class PassInputsDeclarations {
    val imageInputs = mutableListOf<ImageInput>()
    fun imageInput(imageInputConfiguration: ImageInput.() -> Unit) = imageInputs.add(ImageInput().apply(imageInputConfiguration))

    fun ImageInput.texture(assetName: String) = ImageInput.ImageSource.AssetReference(assetName)

    fun ImageInput.renderBuffer(bufferName: String) = ImageInput.ImageSource.RenderBufferReference(bufferName)
}

class PassOutputsDeclaration {
    val outputs = mutableListOf<PassOutput>()
    fun output(dslCode: PassOutput.() -> Unit) = outputs.add(PassOutput().apply(dslCode))
}

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

    ///** Should we copy another buffer to this one before proceeding ? */
    //var copy: String? = null

    enum class BlendMode {
        OVERWRITE,
        ALPHA_TEST,
        MIX,
        ADD,
        PREMULTIPLIED_ALPHA
    }
}

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

