//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.shader

/*interface Shader {
    val name : String

    fun bindUniformBlock(uniformBlockName : String, data : InterfaceBlock) = bindUniformBlock(UniformInput().apply {
        this.name = uniformBlockName
        this.data = data
    })
    fun bindUniformBlock(uniformInput: UniformInput)

    //fun bindImage(imageInput: ImageInput)
}*/

enum class ShaderStage {
    VERTEX,
    GEOMETRY,
    FRAGMENT
}