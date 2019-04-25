package xyz.chunkstories.api.voxel

import org.joml.Vector3fc
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.Model

interface ChunkMeshRenderingInterface {
    fun addModel(model: Model, offset: Vector3fc? = null, materialsOverrides: Map<Int, MeshMaterial> = emptyMap())
}