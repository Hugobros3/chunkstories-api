//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import java.nio.ByteBuffer

/**
 * Represents a mesh, a mesh is a bunch of vertices with N attributes and materials that might be needed in the shader
 * If there is a 'materialId' attribute provided, the shader may index the 'materials' array/list using that
 * If there is a 'boneId' attribute provided, and the ModelInstance supplies an Animation, the shader may perform skeletal animation.
 * */
data class Mesh(val vertices: Int, val attributes: List<MeshAttributeSet>, val material: MeshMaterial)

/** Contains all the per-vertex data for a certain attribute slot (position, normal, color etc) */
data class MeshAttributeSet(val name: String, val components: Int, val format: VertexFormat, val data: ByteBuffer)

data class MeshMaterial(val name: String, val textures: Map<String, String>, val tag: String = "opaque")