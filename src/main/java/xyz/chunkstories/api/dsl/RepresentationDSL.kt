//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.dsl

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.graphics.GraphicsEngine
import xyz.chunkstories.api.graphics.representation.Light
import xyz.chunkstories.api.graphics.representation.ModelInstance
import xyz.chunkstories.api.graphics.representation.Representation
import xyz.chunkstories.api.graphics.representation.RepresentationElement
import org.joml.Vector3d

typealias RepresentationBuildingInstructions = (StaticRepresentationBuildingContext.() -> Unit)

/** A DSL-friendly and opaque way of building a representation.
 * This does not expose any part of the underlying representation, so you cannot update it after it is created.
 *
 * ### Important / Technical note
 * This is used by the engine as an optimisation method, since your representation setup code is really just a lambda
 * calling functions, you can inline it and not actually generate any objects for the representation itself.*/
interface StaticRepresentationBuildingContext {
    val content: Content
    val engine: GraphicsEngine

    /** DSL-helper: Registers a model instance */
    fun model(modelName: String) = model(modelName) {}

    /** DSL-helper: Registers a model instance with additional custom code */
    fun model(modelName: String, code: ModelInstance.() -> Unit) = model(modelName, null, code)

    /** DSL-helper: Registers a model instance in a certain pass with additional custom code */
    fun model(modelName: String, passName: String?, code: ModelInstance.() -> Unit): Any?/* */

    /** DSL-helper: Registers a light */
    fun light(color: Vector3d) = light(color) {}

    /** DSL-helper: Registers a light with additional custom code */
    fun light(color: Vector3d, code: Light.() -> Unit) : Any?

    /** DSL-helper: Registers a bunch of children nodes (those will have the current RepresentationElement as a parent */
    fun RepresentationElement.children(children: RepresentationBuildingInstructions)
}

/** A DSL-friendly and semi-opaque way of building a representation.
 *
 * Unlike the vanilla, static variant, this interface does exposes the underlying objects that make up your representation.
 * You are thus free to capture them in a variables inside your closure and update them using the provided callback. */
interface DynamicRepresentationBuildingContext : StaticRepresentationBuildingContext {
    /** The representation you are currently building. Includes a method to replace it with a new one ! */
    val representation: Representation

    /** The parent of the elements you are adding. Is initially set to representation.root, but there is a FIFO queue
     * in place to handle the nested calls to children {} */
    val parent: RepresentationElement

    /** DSL-helper: Sets up a lambda expression that is called every frame this representation is used */
    fun onEveryFrame(stuff: FrameContext.() -> Unit)

    var ModelInstance.animation : String
        set(value) {
            animator = content.animationsLibrary.getAnimation(value)
        }
        get() = animator?.toString() ?: "No animation"

    override fun model(modelName: String) = model(modelName) {}
    override fun model(modelName: String, code: ModelInstance.() -> Unit) = model(modelName, null, code)
    override fun model(modelName: String, passName: String?, code: ModelInstance.() -> Unit): ModelInstance {
        val model = engine.models[modelName]

        val pass = (if(passName != null) engine.renderGraph.passes[passName] else null) ?: engine.renderGraph.defaultPass
        val modelInstance = ModelInstance(model, pass, parent)
        modelInstance.apply(code)

        parent.children.add(modelInstance)

        return modelInstance
    }

    override fun light(color: Vector3d) = light(color) {}
    override fun light(color: Vector3d, code: Light.() -> Unit) : Light {
        val light = Light(color, parent)
        light.apply(code)

        parent.children.add(light)

        return light
    }
}

interface FrameContext {
    val frameNumber: Int
}