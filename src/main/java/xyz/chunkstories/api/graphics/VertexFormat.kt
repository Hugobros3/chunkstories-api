//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

/** Enumerates the available VertexFormats supported */
enum class VertexFormat constructor(val bytesPerComponent: Int, val normalized: Boolean) {
    FLOAT(4, false),
    HALF_FLOAT(2, false),

    INTEGER(4, false),

    SHORT(2, false),
    USHORT(2, false),
    NORMALIZED_USHORT(2, true),

    BYTE(1, false),
    UBYTE(1, false),
    NORMALIZED_UBYTE(1, true),

    U1010102(1 /** on average */, true),
}

// TODO move to implementation, game API doesn't care about graphics backend
object VertexFormatGL {
    private const val GL_FLOAT = 0x1406
    private const val GL_HALF_FLOAT = 0x140b
    private const val GL_INT = 0x1404
    private const val GL_UNSIGNED_SHORT = 0x1403
    private const val GL_UNSIGNED_BYTE = 0x1401
    private const val GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368
    private const val GL_SHORT = 0x1402
    private const val GL_BYTE = 0x1400

    fun VertexFormat.glMapping(): Int =
            when (this) {
                VertexFormat.FLOAT -> GL_FLOAT
                VertexFormat.HALF_FLOAT -> GL_HALF_FLOAT

                VertexFormat.INTEGER -> GL_INT

                VertexFormat.SHORT -> GL_SHORT
                VertexFormat.USHORT -> GL_UNSIGNED_SHORT
                VertexFormat.NORMALIZED_USHORT -> GL_UNSIGNED_SHORT

                VertexFormat.BYTE -> GL_BYTE
                VertexFormat.UBYTE -> GL_UNSIGNED_BYTE
                VertexFormat.NORMALIZED_UBYTE -> GL_UNSIGNED_BYTE

                VertexFormat.U1010102 -> GL_UNSIGNED_INT_2_10_10_10_REV

                else -> throw Exception("Unmapped vertex format $this")
            }

}

