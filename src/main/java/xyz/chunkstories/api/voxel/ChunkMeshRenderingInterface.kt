//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel

import org.joml.Matrix4f
import org.joml.Vector3fc
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.Model

interface ChunkMeshRenderingInterface {
    fun addModel(model: Model, transformation: Matrix4f? = null, materialsOverrides: Map<Int, MeshMaterial> = emptyMap())
}