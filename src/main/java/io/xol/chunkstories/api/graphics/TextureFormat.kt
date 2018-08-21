//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics

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

// TODO move to implementation, game API doesn't care about graphics backend
object TextureFormatGL {
    const val GL_RGBA = 0x1908
    const val GL_UNSIGNED_BYTE = 0x1401
    const val GL_R11F_G11F_B10F = 0x8c3a
    const val GL_RGB = 0x1907
    const val GL_FLOAT = 0x1406
    const val GL_DEPTH_COMPONENT = 0x1902
    const val GL_DEPTH_COMPONENT24 = 0x81a6
    const val GL_DEPTH_COMPONENT32 = 0x81a7
    const val GL_R32F = 0x822e
    const val GL_RED = 0x1903
    const val GL_RGB10_A2 = 0x8059
    const val GL_RGBA32F = 0x8814
    const val GL_RGBA16F = 34842
    const val GL_R16UI = 0x8234
    const val GL_R16F = 33325
    const val GL_R8UI = 0x8232
    const val GL_R8 = 33321
    const val GL_RG8 = 33323
    const val GL_RG = 33319
    const val GL_RGB8 = 32849
    // public static final int GL_R8 = ;
    const val GL_INT = 0x1404
    const val GL_RED_INTEGER = 0x8d94

    /** OpenGL treats texture formats in this annoying triplets manner so here's a data class to hold them */
    data class GLTextureFormatValues(val internalFormat: Int, val format: Int, val type: Int)

    fun TextureFormat.glMapping() = when (this) {

        TextureFormat.RGBA_8 -> GLTextureFormatValues(GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE)
        TextureFormat.RGB_8 -> GLTextureFormatValues(GL_RGB8, GL_RGB, GL_UNSIGNED_BYTE)
        TextureFormat.RG_8 -> GLTextureFormatValues(GL_RG8, GL_RG, GL_UNSIGNED_BYTE)
        TextureFormat.RED_8 -> GLTextureFormatValues(GL_R8, GL_RG, GL_UNSIGNED_BYTE)
        TextureFormat.RGB_HDR -> GLTextureFormatValues(GL_R11F_G11F_B10F, GL_RGB, GL_FLOAT)
        TextureFormat.DEPTH_32 -> GLTextureFormatValues(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT)
        TextureFormat.DEPTH_24 -> GLTextureFormatValues(GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT, GL_FLOAT)
        TextureFormat.RED_32F -> GLTextureFormatValues(GL_R32F, GL_RED, GL_FLOAT)
        TextureFormat.RED_16I -> GLTextureFormatValues(GL_R16UI, GL_RED_INTEGER, GL_INT)
        TextureFormat.RED_16F -> GLTextureFormatValues(GL_R16F, GL_RED, GL_FLOAT)
        TextureFormat.RGBA_3x10_2 -> GLTextureFormatValues(GL_RGB10_A2, GL_RGBA, GL_UNSIGNED_BYTE)
        TextureFormat.RGBA_16F -> GLTextureFormatValues(GL_RGBA16F, GL_RGBA, GL_FLOAT)
        TextureFormat.RGBA_32F -> GLTextureFormatValues(GL_RGBA32F, GL_RGBA, GL_FLOAT)
        TextureFormat.RED_8UI -> GLTextureFormatValues(GL_R8UI, GL_RED_INTEGER, GL_INT)
    }
}