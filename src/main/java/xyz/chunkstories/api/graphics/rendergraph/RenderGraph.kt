//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import org.joml.Vector2i

class RenderGraphDeclaration {
    val renderTasks = mutableMapOf<String, RenderTaskDeclaration>()

    fun renderTask(dslCode: RenderTaskDeclaration.() -> Unit) {
        val task = RenderTaskDeclaration().apply(dslCode)
        renderTasks.put(task.name, task)
    }

    val viewportSize : RenderBufferSize.ViewportRelativeSize
        get() = RenderBufferSize.ViewportRelativeSize(1f, 1f)
}