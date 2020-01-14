//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

fun renderGraph(dslCode: RenderGraphDeclaration.() -> Unit) = dslCode

class RenderGraphDeclaration {
    val renderTasks = mutableMapOf<String, RenderTaskDeclaration>()

    fun renderTask(dslCode: RenderTaskDeclaration.() -> Unit) {
        val task = RenderTaskDeclaration().apply(dslCode)
        renderTasks[task.name] = task
    }

    val frameSetupHooks = mutableListOf<Frame.() -> Unit>()
    fun setup(frameSetup: Frame.() -> Unit) {
        frameSetupHooks.add(frameSetup)
    }

    val viewportSize : RenderBufferSize.ViewportRelativeSize
        get() = RenderBufferSize.ViewportRelativeSize(1f, 1f)
}