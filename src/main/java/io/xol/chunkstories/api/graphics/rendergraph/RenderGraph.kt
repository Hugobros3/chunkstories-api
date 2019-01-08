//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics.rendergraph

import org.joml.Vector2i

/** Interface to the (abstract) render graph of the renderer */
interface RenderGraph {
    val buffers : Map<String, RenderBuffer>
    val passes : Map<String, Pass>

    /** Returns the pass declared with `default = true` */
    val defaultPass: Pass

    val finalPass: Pass

    val viewportSize: Vector2i
}