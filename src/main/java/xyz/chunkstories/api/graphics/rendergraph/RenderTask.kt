package xyz.chunkstories.api.graphics.rendergraph

class RenderTaskDeclaration {
    lateinit var name: String

    lateinit var finalPass: String

    lateinit var renderBuffersDeclarations : RenderBuffersDeclarations
    fun renderBuffers(dslCode: RenderBuffersDeclarations.() -> Unit) {
        renderBuffersDeclarations = RenderBuffersDeclarations().apply(dslCode)
    }

    lateinit var passesDeclarations: PassesDeclarations
    fun passes(dslCode: PassesDeclarations.() -> Unit) {
        passesDeclarations = PassesDeclarations().apply(dslCode)
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