//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.graphics.GraphicsEngine
import io.xol.chunkstories.api.graphics.ImageInput
import io.xol.chunkstories.api.graphics.Texture
import io.xol.chunkstories.api.graphics.UniformInput
import io.xol.chunkstories.api.graphics.rendergraph.*
import io.xol.chunkstories.api.graphics.systems.drawing.DrawingSystem
import io.xol.chunkstories.api.graphics.systems.drawing.FullscreenQuadDrawer
import org.joml.Vector2i
import kotlin.math.roundToInt
import kotlin.reflect.KClass

typealias RenderGraphDeclarationScript = RenderGraphDeclarationsContext.() -> Unit

interface RenderGraphDeclarationsContext {
    val renderGraph: RenderGraph

    fun renderBuffers(renderBufferDeclarations: RenderBuffersDeclarationCtx.() -> Unit) : RenderBuffersDeclarationCtx

    fun passes(function: PassesDeclarationCtx.() -> Unit) : PassesDeclarationCtx

    fun ImageInput.texture(assetName: String) = ImageInput.ImageSource.AssetReference(assetName)

    fun ImageInput.renderBuffer(bufferName: String) = ImageInput.ImageSource.RenderBufferReference(bufferName)

    val viewportSize
        get() = renderGraph.viewportSize
}

interface RenderBuffersDeclarationCtx {
    fun renderBuffer(renderBufferConfiguration: RenderBuffer.() -> Unit)

    /** Syntactic sugar to not have to write Vector2i(width, height) in scripts */
    infix fun Int.by(b: Int): Vector2i = Vector2i(this, b)

    /** Syntactic sugar to allow scaling buffers in a cool syntax */
    operator fun Vector2i.times(s: Double): Vector2i = Vector2i((this.x * s).roundToInt(), (this.y * s).roundToInt())
}

interface PassesDeclarationCtx {
    fun pass(function: Pass.() -> Unit)

    /** DSL-style syntactic sugar, shorthand for dependencies.addAll() */
    fun Pass.dependsOn(vararg pass: String) = dependencies.addAll(pass)

    /** DSL-style syntactic sugar, takes input declarations and adds them to the pass object */
    fun Pass.inputs(inputsDeclarations: PassInputsDeclarationsCtx.() -> Unit) = object : PassInputsDeclarationsCtx {
        override fun imageInput(imageInputConfiguration: ImageInput.() -> Unit) {
            val imageInput = ImageInput().apply(imageInputConfiguration)
            this@inputs.imageInputs.add(imageInput)
        }

        override fun uniformInput(uniformInputConfiguration: UniformInput.() -> Unit) {
            val uniformInput = UniformInput().apply(uniformInputConfiguration)
            this@inputs.uniformInputs.add(uniformInput)

        }
    }.apply(inputsDeclarations)

    /** DSL-style syntactic sugar, configures the depth testing for the pass */
    fun Pass.depth(configuration: DepthTestingConfiguration.() -> Unit) {
        this.depth = DepthTestingConfiguration().apply(configuration)
    }

    /** DSL-style syntactic sugar, takes output declarations and adds them to the pass object */
    fun Pass.outputs(outputsDeclarations : PassOutputsDeclarationCtx.() -> Unit) = object : PassOutputsDeclarationCtx {
        override fun output(outputConfiguration: PassOutput.() -> Unit) {
            val output = PassOutput().apply(outputConfiguration)
            this@outputs.outputs.add(output)
        }
    }

    fun Pass.draws(drawsDeclarations : DrawsDeclarationCtx.() -> Unit)
}

interface PassInputsDeclarationsCtx {
    fun imageInput(imageInputConfiguration: ImageInput.() -> Unit)

    fun uniformInput(uniformInputConfiguration : UniformInput.() -> Unit)
}

interface PassOutputsDeclarationCtx {
    fun output(outputConfiguration: PassOutput.() -> Unit)
}

interface DrawsDeclarationCtx {
    fun fullscreenQuad() = system(FullscreenQuadDrawer::class)

    fun <T: DrawingSystem> system(type: KClass<T>) = system(type) {}
    fun <T: DrawingSystem> system(type: KClass<T>, config: (T.() -> Unit))

    //fun <T: DrawingSystem> system(type: Class<T>, config: T.() -> Unit)
}