package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.rendergraph.RenderGraph

interface GraphicsEngine {
    val renderGraph : RenderGraph
}