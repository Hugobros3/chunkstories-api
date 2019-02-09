package xyz.chunkstories.api.graphics.systems

class RegisteredGraphicSystem<T : GraphicSystem>(val clazz: Class<T>, val dslCode: (T.() -> Unit) ) {
    //var init: (T.() -> Unit)? = null
    //var input: (ShaderBindingInterface.() -> Unit)? = null
}