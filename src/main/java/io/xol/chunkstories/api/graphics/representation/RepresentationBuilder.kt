package io.xol.chunkstories.api.graphics.representation

import io.xol.chunkstories.api.client.ClientContent
import io.xol.chunkstories.api.graphics.GraphicsEngine
import org.joml.Vector3d

typealias RepresentationBuildingInstructions = (RepresentationBuildingCtx.() -> Unit)

/** The context you use to build/edit a init */
interface RepresentationBuildingCtx {
    val parent : RepresentationElement?
    val representation : MutableList<RepresentationElement>

    val content : ClientContent
    val engine : GraphicsEngine

    /** DSL-helper: Registers a model instance */
    fun modelInstance(modelName: String) = modelInstance(modelName) {}

    /** DSL-helper: Registers a model instance with additional custom code */
    fun modelInstance(modelName: String, code: ModelInstance.() -> Unit) = modelInstance(modelName, null, code)

    /** DSL-helper: Registers a model instance in a certain pass with additional custom code */
    fun modelInstance(modelName: String, passName : String?, code: ModelInstance.() -> Unit) {
        val model = engine.models[modelName]

        val pass = (if(passName != null) engine.renderGraph.passes[passName] else null) ?: engine.renderGraph.defaultPass
        val modelInstance = ModelInstance(model, pass)

        modelInstance.parentObject = parent
        modelInstance.apply(code)

        representation.add(modelInstance)
    }

    /** DSL-helper: Registers a light */
    fun light(color: Vector3d) = light(color) {}

    /** DSL-helper: Registers a light with additional custom code */
    fun light(color : Vector3d, code : Light.() -> Unit) {
        val light = Light(color)

        light.parentObject = parent
        light.apply(code)

        representation.add(light)
    }

    fun onEveryFrame(stuff : FrameContext.() -> Unit)

    /** DSL-helper: Registers a bunch of children nodes (those will have the current RepresentationElement as a parent */
    fun RepresentationElement.children(children : RepresentationBuildingInstructions)
}

interface FrameContext {
    val frameNumber: Int
}