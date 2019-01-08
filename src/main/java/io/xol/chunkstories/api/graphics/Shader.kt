//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.structs.InterfaceBlock

interface Shader {
    val name : String

    fun bindUniformBlock(uniformBlockName : String, data : InterfaceBlock) = bindUniformBlock(UniformInput().apply {
        this.name = uniformBlockName
        this.data = data
    })
    fun bindUniformBlock(uniformInput: UniformInput)

    //fun bindImage(imageInput: ImageInput)
}

enum class ShaderStage {
    VERTEX,
    GEOMETRY,
    FRAGMENT
}

class UniformInput {
    lateinit var name: String
    lateinit var data: InterfaceBlock
}

class ImageInput {
    /** Name of the sampler this will bind to */
    lateinit var name: String

    /** Name of the source RenderBuffer or a path to an asset, or a Texture object. */
    lateinit var source: ImageSource

    sealed class ImageSource {
        class AssetReference(val assetName: String) : ImageSource()
        class RenderBufferReference(val renderBufferName: String) : ImageSource()
        class TextureReference(val texture: Texture) : ImageSource()
    }

    // General state fluff
    var samplingMode = SamplingMode.NEAREST
    var mipmapping = false
    var wrapping = false

    enum class SamplingMode {
        LINEAR,
        NEAREST
    }
}