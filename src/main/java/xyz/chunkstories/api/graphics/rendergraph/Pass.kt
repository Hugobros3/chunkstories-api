//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import org.joml.Vector4d
import xyz.chunkstories.api.graphics.Texture
import xyz.chunkstories.api.graphics.TextureTilingMode

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

    val setupLambdas = mutableListOf<PassInstance.() -> Unit>()
    fun setup(dslCode: PassInstance.() -> Unit) {
        setupLambdas.add(dslCode)
    }
    /*var inputs: PassInputsDeclarations? = null
    fun inputs(dslCode: PassInputsDeclarations.() -> Unit) {
        inputs = PassInputsDeclarations().also(dslCode)
    }*/
}

/*class PassInputsDeclarations {
    val imageInputs = mutableListOf<ImageInput>()
    fun imageInput(imageInputConfiguration: ImageInput.() -> Unit) = imageInputs.add(ImageInput().apply(imageInputConfiguration))
}*/

class ImageInput {
    ///** Name of the sampler this will bind to */
    //lateinit var name: String

    /** Name of the source RenderBuffer or a path to an asset, or a Texture object. */
    lateinit var source: ImageSource

    // General state fluff
    var scalingMode = ScalingMode.NEAREST
    var tilingMode = TextureTilingMode.CLAMP_TO_EDGE
    var depthCompareMode = DepthCompareMode.DISABLED

    var mipmapping = false

    enum class ScalingMode {
        LINEAR,
        NEAREST
    }

    enum class DepthCompareMode {
        DISABLED,
        GREATER,
        GREATER_OR_EQUAL,
        EQUAL,
        LESS_OR_EQUAL,
        LESS
    }
}

fun asset(assetName: String) = ImageSource.AssetReference(assetName)
fun renderBuffer(bufferName: String) = ImageSource.RenderBufferReference(bufferName)

sealed class ImageSource {
    class AssetReference(val assetName: String) : ImageSource()
    class RenderBufferReference(val renderBufferName: String) : ImageSource()
    class TextureReference(val texture: Texture) : ImageSource()
    class TaskOutput(val context: RenderTaskInstance, val output: PassOutput) : ImageSource()
    class TaskOutputDepth(val context: RenderTaskInstance) : ImageSource()
}

class PassOutputsDeclaration {
    val outputs = mutableListOf<PassOutput>()
    fun output(dslCode: PassOutput.() -> Unit) = outputs.add(PassOutput().apply(dslCode))

    fun PassOutput.renderBuffer(bufferName: String) = RenderTarget.RenderBufferReference(bufferName)
    fun PassOutput.taskInput(bufferName: String) = RenderTarget.TaskInput(bufferName)
}

/** Represents one output to a pass, includes some configuration */
class PassOutput {
    lateinit var name: String

    /** If the output buffer has a different name than the shader output, supply it here */
    //var outputBuffer: String? = null
    var target: RenderTarget? = null

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

sealed class RenderTarget {
    object BackBuffer : RenderTarget()
    class RenderBufferReference(val renderBufferName: String) : RenderTarget()
    class TaskInput(val name: String) : RenderTarget()
}

class DepthTestingConfiguration {
    /** Disables depth testing altogether */
    var enabled = true

    /** Should we clear the buffer before proceeding ? */
    var clear = false
    var clearValue = 0f

    /** Disable to stop the zBuffer from updating */
    var write = true

    /** If enabled is set to true this has to be configured. */
    var depthBuffer: RenderTarget? = null

    fun renderBuffer(bufferName: String) = RenderTarget.RenderBufferReference(bufferName)
    fun taskInput(bufferName: String) = RenderTarget.TaskInput(bufferName)

    var mode: DepthTestMode = DepthTestMode.GREATER

    enum class DepthTestMode {
        GREATER, GREATER_OR_EQUAL, EQUAL, LESS_OR_EQUAL, LESS, ALWAYS
    }
}

val noDepthTest = DepthTestingConfiguration().apply { enabled = false }

