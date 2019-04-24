//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.rendergraph

import xyz.chunkstories.api.graphics.Texture2D
import xyz.chunkstories.api.graphics.TextureFormat
import org.joml.Vector2i

class RenderBufferDeclaration {
    lateinit var name: String
    lateinit var format: TextureFormat
    lateinit var size: RenderBufferSize

    /** Syntactic sugar to not have to write Vector2i(width, height) in scripts */
    infix fun Int.by(b: Int) = RenderBufferSize.FixedSize(this, b)

    /** Syntactic sugar to allow scaling buffers in a cool syntax */
    operator fun RenderBufferSize.ViewportRelativeSize.times(s: Double) = RenderBufferSize.ViewportRelativeSize((this.scaleHorizontal * s.toFloat()), (this.scaleVertical * s.toFloat()))
}

sealed class RenderBufferSize {
    data class FixedSize(val width: Int, val height: Int) : RenderBufferSize()
    data class ViewportRelativeSize(val scaleHorizontal: Float, val scaleVertical: Float) : RenderBufferSize()
}