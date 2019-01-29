package xyz.chunkstories.api.graphics.systems

import xyz.chunkstories.api.graphics.rendergraph.ShaderBindingInterface

class RegisteredGraphicSystem<T : GraphicSystem>(val clazz: Class<T>) {
    var init: (T.() -> Unit)? = null
    var input: (ShaderBindingInterface.() -> Unit)? = null
}