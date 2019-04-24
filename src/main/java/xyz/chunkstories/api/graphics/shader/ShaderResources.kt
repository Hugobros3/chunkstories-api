package xyz.chunkstories.api.graphics.shader

import xyz.chunkstories.api.graphics.rendergraph.ImageInput
import xyz.chunkstories.api.graphics.rendergraph.ImageSource
import xyz.chunkstories.api.graphics.structs.InterfaceBlock

class ShaderResources(val parent: ShaderResources?) {
    val images = mutableListOf<Triple<String, Int, ImageInput>>()
    val ubos = mutableListOf<Pair<String?, InterfaceBlock>>()

    fun supplyImage(imageName: String, code: ImageInput.() -> Unit) = supplyImage(imageName, ImageInput().apply(code))

    fun supplyImage(imageName: String, imageInput: ImageInput) = supplyImage(imageName, 0, imageInput)

    fun supplyImage(imageName: String, arrayIndex: Int, imageInput: ImageInput) {
        images += Triple(imageName, arrayIndex, imageInput)
    }

    fun supplyUniformBlock(name: String? = null, contents: InterfaceBlock) {
        ubos += Pair(name, contents)
    }
}