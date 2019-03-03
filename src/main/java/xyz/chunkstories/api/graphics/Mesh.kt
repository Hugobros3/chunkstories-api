//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import java.nio.ByteBuffer

/**
 *
 * */
data class Mesh(val vertices: Int, val attributes: List<MeshAttributeSet>, val material: MeshMaterial, val boneIds: Map<String, Int>?)

/** Contains all the per-vertex data for a certain attribute slot (position, normal, color etc) */
data class MeshAttributeSet(val name: String, val components: Int, val format: VertexFormat, val data: ByteBuffer)

data class MeshMaterial(val name: String, val textures: Map<String, String>, val tag: String = "opaque")