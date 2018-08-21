package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.structs.InterfaceBlock

interface Shader {
    val name : String

    fun bindUniformBlock(uniformBlockName : String, data : InterfaceBlock)
}

