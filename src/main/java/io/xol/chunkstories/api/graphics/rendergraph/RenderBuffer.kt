package io.xol.chunkstories.api.graphics.rendergraph

import io.xol.chunkstories.api.graphics.Texture2D
import io.xol.chunkstories.api.graphics.TextureFormat
import org.joml.Vector2i

/** A 2d graphical buffer containing some visual information used in passes.
 * It can be used as input to a shader. */
class RenderBuffer {
    constructor()
    constructor(name: String, format: TextureFormat, size: Vector2i) {
        this.name = name
        this.format = format
        this.size = size
    }

    var name = "unnamed"
    var format = TextureFormat.RGBA_8
    var size: Vector2i = Vector2i(512, 512)

    /** Gets initialized once the graph is completed */
    lateinit var texture: Texture2D

    override fun toString(): String {
        return "RenderBuffer($name, $format, $size)"
    }
}