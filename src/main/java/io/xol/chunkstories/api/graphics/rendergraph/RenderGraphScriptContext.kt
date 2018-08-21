package io.xol.chunkstories.api.graphics.rendergraph

import org.joml.Vector2i
import kotlin.math.roundToInt

typealias RenderGraphDeclarationScript = RenderGraphDeclarationsCtx.() -> Unit

/** Fancy adapter for the rendergraph DSL (which is really just Kotlin with this syntactic sugar)*/
class RenderGraphDeclarationsCtx(val renderGraph: RenderGraph) {

    /** Execute the script code from a render graph declaration script */
    fun execute(script: RenderGraphDeclarationScript) {
        this.apply(script)
    }

    /** Enter the context to declare a bunch of RenderBuffers */
    fun renderBuffers(renderBufferDeclarations: RenderBuffersDeclarationCtx.() -> Unit) = object : RenderBuffersDeclarationCtx {

        /** Declare a render buffer and add it to the graph */
        override fun renderBuffer(renderBufferConfiguration: RenderBuffer.() -> Unit) {
            val renderBuffer = RenderBuffer().apply(renderBufferConfiguration)
            renderGraph.buffers.add(renderBuffer)
        }
    }.apply(renderBufferDeclarations)

    /** Enter the context to declare a bunch of Passes */
    fun passes(function: PassesDeclarationCtx.() -> Unit) = object : PassesDeclarationCtx {

        /** Declare a pass and add it to the graph */
        override fun pass(function: Pass.() -> Unit) {
            val pass = Pass().apply(function)
            renderGraph.passes.add(pass)
        }
    }.apply(function)

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
}

interface PassInputsDeclarationsCtx {
    fun imageInput(imageInputConfiguration: ImageInput.() -> Unit)
}

interface PassOutputsDeclarationCtx {
    fun output(outputConfiguration: PassOutput.() -> Unit)
}