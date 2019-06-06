//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.systems

class RegisteredGraphicSystem<T : GraphicSystem>(val clazz: Class<T>, val dslCode: (T.() -> Unit) ) {
    //var init: (T.() -> Unit)? = null
    //var input: (ShaderBindingInterface.() -> Unit)? = null
}