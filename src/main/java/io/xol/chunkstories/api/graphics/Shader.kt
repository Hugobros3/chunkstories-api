package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.rendergraph.ImageInput
import io.xol.chunkstories.api.graphics.rendergraph.UniformInput
import io.xol.chunkstories.api.graphics.structs.InterfaceBlock

interface Shader {
    val name : String

    fun bindUniformBlock(uniformBlockName : String, data : InterfaceBlock) = bindUniformBlock(UniformInput().apply {
        this.name = uniformBlockName
        this.data = data
    })
    fun bindUniformBlock(uniformInput: UniformInput)

    fun bindImage(imageInput: ImageInput)
}

