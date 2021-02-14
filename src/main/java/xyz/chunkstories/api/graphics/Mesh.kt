//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

data class Mesh(val vertices: Int, val attributes: List<MeshAttributeSet>, val material: MeshMaterial, val boneIds: Map<String, Int>?) {
    // For simplicity and speed we assume each mesh is different
    val uuid = UUID.randomUUID()!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mesh

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}

/** Contains all the per-vertex data for a certain attribute slot (position, normal, color etc) */
data class MeshAttributeSet(val name: String, val components: Int, val format: VertexFormat, val data: ByteBuffer) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other) && (other as MeshAttributeSet).data == data
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + components
        result = 31 * result + format.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}

data class MeshMaterial(val name: String, val textures: Map<String, String>, val tag: String = "opaque")

fun Mesh.reverseWindingOrder() : Mesh {
    val reversedAttributes = attributes.map {
        val reversedByteBuffer = ByteBuffer.allocateDirect(it.data.capacity())

        val bytesPerVertex = it.format.bytesPerComponent * it.components

        for(i in 0 until it.data.capacity() / bytesPerVertex) {
            val ifl = (i / 3) * 3
            //val irr = 0

            val t = if(i % 3 == 0) ifl else if(i % 3 == 1) ifl + 2 else ifl + 1

            val tb = t * bytesPerVertex
            for(b in 0 until bytesPerVertex) {
                reversedByteBuffer.put(it.data.get(tb + b))
            }
        }
        //reversedByteBuffer.flip()

        reversedByteBuffer.order(ByteOrder.LITTLE_ENDIAN)

        MeshAttributeSet(it.name, it.components, it.format, reversedByteBuffer)
    }

    return Mesh(vertices, reversedAttributes, material, boneIds)
}