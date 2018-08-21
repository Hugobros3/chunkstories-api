package io.xol.chunkstories.api.graphics.rendergraph

import org.joml.Vector2i

/** Interface to the (abstract) render graph of the renderer */
interface RenderGraph {
    val buffers : MutableListOfNamedStuff<RenderBuffer>
    val passes : MutableListOfNamedStuff<Pass>

    /** Returns the pass declared with `default = true` */
    val defaultPass: Pass

    val viewportSize: Vector2i
}

interface MutableListOfNamedStuff<T> : MutableList<T> {
    operator fun get(name : String) : T?
}