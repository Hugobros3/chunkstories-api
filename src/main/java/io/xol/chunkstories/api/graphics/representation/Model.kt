//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics.representation

import io.xol.chunkstories.api.animation.Animation
import io.xol.chunkstories.api.animation.Animator
import io.xol.chunkstories.api.graphics.Mesh
import io.xol.chunkstories.api.graphics.rendergraph.Pass
import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import io.xol.chunkstories.api.physics.Box
import org.joml.Vector3d

/** One instance of a particular mesh */
data class ModelInstance(val model: Model, val pass: Pass, override val parentObject: RepresentationElement?) : RepresentationElement(parentObject) {
    constructor(model: Model, pass: Pass) : this(model, pass, null)

    /** Allows for overriding the default materials of that mesh */
    val materialsBindings = LinkedHashMap<String, Surface>()

    var animator: Animator? = null
    var instanceData: InterfaceBlock? = null
}

/** Completes a Mesh with a bunch of metadata */
data class Model(val mesh: Mesh, val skeleton: Animation?) {

    /** Used for frustrum culling, might be modified to solve popping issues */
    var boundingBox: Box = Box(Vector3d(), Vector3d(5.0, 5.0, 5.0))
}