//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Matrix4f
import xyz.chunkstories.api.animation.Animation
import xyz.chunkstories.api.animation.Animator
import xyz.chunkstories.api.graphics.Mesh
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.structs.InterfaceBlock

data class Model(val meshes: List<Mesh>)

/** One instance of a particular model */
data class ModelInstance(val model: Model,
                         val position: ModelPosition,
                         val materials: Map<Int, MeshMaterial> = emptyMap(),
                         val meshesMask: Int = -1,
                         val animator: Animator? = null
) : Representation {
}

data class ModelPosition(val matrix: Matrix4f = Matrix4f()) : InterfaceBlock