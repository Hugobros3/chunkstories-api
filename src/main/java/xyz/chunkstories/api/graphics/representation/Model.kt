//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Matrix4f
import org.joml.Vector3dc
import org.joml.Vector3fc
import xyz.chunkstories.api.animation.Animation
import xyz.chunkstories.api.graphics.Mesh

/** One instance of a particular mesh */
data class ModelInstance(val mesh: Mesh, val transformation: ObjectTransformation = ObjectTransformation()) : Representation {
}

data class ObjectTransformation(var matrix: Matrix4f = Matrix4f()) {
    constructor(position: Vector3dc) : this() {
        matrix.translate(position.x().toFloat(), position.y().toFloat(), position.z().toFloat())
    }

    constructor(position: Vector3fc) : this() {
        matrix.translate(position.x(), position.y(), position.z())
    }
}

/** Completes a Mesh with a bunch of metadata */
data class Model(val mesh: Mesh, val skeleton: Animation?) {

    ///** Used for frustrum culling, might be modified to solve popping issues */
    //var boundingBox: Box = Box(Vector3d(), Vector3d(5.0, 5.0, 5.0))
}