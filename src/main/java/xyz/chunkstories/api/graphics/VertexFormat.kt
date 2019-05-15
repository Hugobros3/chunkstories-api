//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

/** Enumerates the available VertexFormats supported */
enum class VertexFormat constructor(val bytesPerComponent: Int) {
    FLOAT(4),
    HALF_FLOAT(2),

    INT(4),
    UINT(4),

    SHORT(2),
    USHORT(2),
    NORMALIZED_SHORT(2),
    NORMALIZED_USHORT(2),

    BYTE(1),
    UBYTE(1),
    NORMALIZED_BYTE(1),
    NORMALIZED_UBYTE(1),

    U1010102(1 /** on average */),
}