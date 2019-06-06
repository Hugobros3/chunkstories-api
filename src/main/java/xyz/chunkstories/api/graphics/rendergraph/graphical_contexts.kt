//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import org.joml.Vector2i
import xyz.chunkstories.api.graphics.shader.ShaderResources
import xyz.chunkstories.api.graphics.structs.Camera

interface Frame {
    val frameNumber: Int

    val shaderResources: ShaderResources
}

interface RenderTaskInstance {
    val name: String
    val requester: PassInstance?

    val frame: Frame

    val declaration: RenderTaskDeclaration

    val camera: Camera

    val parameters: Map<String, Any>
    val artifacts: MutableMap<String, Any>
}

interface PassInstance {
    val taskInstance: RenderTaskInstance

    val declaration: PassDeclaration

    val shaderResources: ShaderResources
    val renderTargetSize: Vector2i

    fun dispatchRenderTask(taskInstanceName: String, camera: Camera, renderTaskName: String, parameters: Map<String, Any>, callback: (RenderTaskInstance) -> Unit)
}

interface SystemExecutionContext {
    val passInstance: PassInstance

    val shaderResources: ShaderResources
}