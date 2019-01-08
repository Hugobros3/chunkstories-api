//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics.rendergraph

import io.xol.chunkstories.api.graphics.Texture2D
import io.xol.chunkstories.api.graphics.TextureFormat
import org.joml.Vector2i

/** A 2d graphical buffer containing some visual information used in passes.
 * It can be used as input to a shader. */
abstract class RenderBuffer {
    var name = "unnamed"
    var format = TextureFormat.RGBA_8
    var size: Vector2i = Vector2i(512, 512)

    /** Gets initialized once the graph is completed */
    abstract val texture: Texture2D

    override fun toString(): String {
        return "RenderBuffer($name, $format, $size)"
    }
}