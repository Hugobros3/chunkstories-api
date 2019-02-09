package xyz.chunkstories.api.graphics.rendergraph

import xyz.chunkstories.api.graphics.structs.Camera
import xyz.chunkstories.api.graphics.structs.InterfaceBlock

interface RenderingContext {
    val name: String
    val task: RenderTaskDeclaration

    val camera: Camera

    val parameters: Map<String, Any>
    val artifacts: MutableMap<String, Any>
}

interface PassInstance {
    val pass: PassDeclaration
    val context: RenderingContext

    fun dispatchRenderTask(taskInstanceName: String, camera: Camera, renderTaskName: String, parameters: Map<String, Any>, callback: (RenderingContext) -> Unit)
}

/*interface ShaderBindingInterface {
    val context: RenderingContext

    fun supplyUniformBlock(name : String? = null, interfaceBlock: InterfaceBlock)

    fun supplyTexture2DAsset(name: String, assetPath: String)
    fun supplyTexture2DRenderBuffer(name: String, assetPath: String)
}*/