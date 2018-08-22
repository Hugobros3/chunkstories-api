package io.xol.chunkstories.api.graphics.representation

import io.xol.chunkstories.api.animation.SkeletalAnimation
import io.xol.chunkstories.api.animation.SkeletonAnimator
import io.xol.chunkstories.api.graphics.rendergraph.Pass
import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import io.xol.chunkstories.api.mesh.Mesh
import io.xol.chunkstories.api.physics.CollisionBox
import org.joml.Vector3d

/** One instance of a particular mesh */
data class ModelInstance(val model: Model, val pass: Pass) : RepresentationElement() {
    /** Allows for overriding the default materials of that mesh */
    val materialsBindings = LinkedHashMap<String, Surface>()

    var animation: SkeletonAnimator? = null
    var instanceData: InterfaceBlock? = null
}

/** Completes a Mesh with a bunch of metadata */
data class Model(val mesh: Mesh, val skeleton: SkeletalAnimation?) {

    /** Used for frustrum culling, might be modified to solve popping issues */
    var boundingBox: CollisionBox = CollisionBox(Vector3d(), Vector3d(5.0, 5.0, 5.0))
}