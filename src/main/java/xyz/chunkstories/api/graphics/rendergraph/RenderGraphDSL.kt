//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import xyz.chunkstories.api.graphics.systems.drawing.DrawingSystem
import xyz.chunkstories.api.graphics.systems.drawing.FullscreenQuadDrawer
import xyz.chunkstories.api.graphics.*
import xyz.chunkstories.api.graphics.systems.GraphicSystem
import xyz.chunkstories.api.graphics.systems.RegisteredGraphicSystem
import kotlin.reflect.KClass

typealias RenderGraphDeclarationScript = RenderGraphDeclaration.() -> Unit

class DrawsDeclarations {
    val registeredSystems = mutableListOf<RegisteredGraphicSystem<*>>()
    fun <T: GraphicSystem> system(type: KClass<T>, dslCode: (T.() -> Unit)) = registeredSystems.add(RegisteredGraphicSystem(type.java, dslCode))

    /*fun <T: GraphicSystem> RegisteredGraphicSystem<T>.init(initCode: T.() -> Unit) {
        this.init = initCode
    }

    fun <T: GraphicSystem> RegisteredGraphicSystem<T>.inputs(inputsCode: ShaderBindingInterface.() -> Unit) {
        this.input = inputsCode
    }*/

    fun <T: GraphicSystem> system(type: KClass<T>) = system(type) {}
    fun fullscreenQuad() = system(FullscreenQuadDrawer::class)

    //fun <T: DrawingSystem> system(type: Class<T>, config: T.() -> Unit)
}