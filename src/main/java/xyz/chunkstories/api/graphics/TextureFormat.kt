//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

enum class TextureFormat constructor(val bytesPerTexel: Int) {
    /** Classic OpenGL format: 8-bit per component, 4 components 32-bit per pixel. */
    RGBA_8(4),

    /** Three 8-bit components (unsigned, normalized)  */
    RGB_8(3),

    /** Two 8-bit components (unsigned, normalized)  */
    RG_8(2),

    /** A single 8-bit component (unsigned, normalized)  */
    RED_8(1),

    /** "HDR" format for the default renderbuffer, uses 3 floating-point component */
    RGB_HDR(4),

    /** High precision depth format (32-bit)  */
    DEPTH_32(4),

    /** Lesser precision depth format (24-bit)  */
    DEPTH_24(3),

    /** One component of 32-bit floating point (very precise)  */
    RED_32F(4),

    /** One component of 16-bit integers  */
    RED_16I(2),

    /** One component of 16-bit float  */
    RED_16F(2),

    /** 10 bits for RGB, two for alpha. Normalized.  */
    RGBA_3x10_2(4),

    /** High precision HDR format, 16-bit per component, quite heavy.  */
    RGBA_16F(16),

    /** Very high precision HDR format, fully fat 32-bit per component, slow !  */
    RGBA_32F(16),

    /** A single 8-bit component (unsigned)  */
    RED_8UI(1);
}