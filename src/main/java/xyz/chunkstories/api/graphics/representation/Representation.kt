//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import xyz.chunkstories.api.dsl.DynamicRepresentationBuildingContext
import xyz.chunkstories.api.dsl.RepresentationBuildingInstructions
import org.joml.Matrix4d

open class RepresentationElement(parentObject: RepresentationElement?) {
    open val parentObject : RepresentationElement?

    init {
        if(parentObject != null) {
            parentObject.children.add(this)
            this.parentObject = parentObject
        } else {
            this.parentObject = null
        }
    }

    /** The transformation matrix for that object */
    var matrix = Matrix4d()

    /** If enabled, the transformation matrix of this object will be multiplied by the parent one. */
    var inheritParentTransformation = true
    val children = mutableListOf<RepresentationElement>()
}

/** An opaque interface to whatever internal data structure the engine has for these.
 * The engine hands you one for the objects that can be represented and lets you do whatever you wish with it. */
interface Representation : List<RepresentationElement> {
    /** The root object does not represent anything but it's transformation matrix is based on the represented object's
     * properties */
    val root : RepresentationElement

    /** Some fields of the representation can be adjusted ( everything immutable in theory. )
     * However you may want to tweak the representation more (like adding/removing models or changing the target
     * pass, and for those you need to rebuild the representation */
    fun rebuildRepresentation(representationBuildingInstructions: DynamicRepresentationBuildingContext.() -> Unit)

    /** You may want to run something every frame. If that is the case, you can use supply instructions in this field */
    var everyFrame : RepresentationBuildingInstructions?
}