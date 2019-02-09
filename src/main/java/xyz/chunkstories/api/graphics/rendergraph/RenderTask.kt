package xyz.chunkstories.api.graphics.rendergraph

import xyz.chunkstories.api.graphics.TextureFormat

class RenderTaskDeclaration {
    lateinit var name: String

    lateinit var finalPassName: String
    val finalPass: PassDeclaration
        get() = passesDeclarations.passes.find { it.name == finalPassName }!!

    lateinit var renderBuffersDeclarations : RenderBuffersDeclarations
    fun renderBuffers(dslCode: RenderBuffersDeclarations.() -> Unit) {
        renderBuffersDeclarations = RenderBuffersDeclarations().apply(dslCode)
    }

    lateinit var passesDeclarations: PassesDeclarations
    fun passes(dslCode: PassesDeclarations.() -> Unit) {
        passesDeclarations = PassesDeclarations().apply(dslCode)
    }

    var inputs: RenderTaskInputs? = null
    fun taskInputs(dslCode: RenderTaskInputs.() -> Unit) {
        inputs = RenderTaskInputs().apply(dslCode)
    }
}

class RenderBuffersDeclarations {
    val renderBuffers = mutableListOf<RenderBufferDeclaration>()

    fun renderBuffer(dslCode: RenderBufferDeclaration.() -> Unit) = renderBuffers.add(RenderBufferDeclaration().apply(dslCode))
}

class PassesDeclarations {
    val passes = mutableListOf<PassDeclaration>()

    fun pass(dslCode: PassDeclaration.() -> Unit) = passes.add(PassDeclaration().apply(dslCode))
}

class RenderTaskInputs {
    val inputs = mutableListOf<RenderTaskInput>()

    fun input(dslCode: RenderTaskInput.() -> Unit) {
        inputs.add(RenderTaskInput().apply(dslCode))
    }
}

class RenderTaskInput {
    lateinit var name: String
    lateinit var format: TextureFormat
}
