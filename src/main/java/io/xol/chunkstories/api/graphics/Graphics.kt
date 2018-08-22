package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.rendergraph.RenderGraph
import io.xol.chunkstories.api.graphics.representation.Model

interface GraphicsEngine {
    val renderGraph : RenderGraph

    val textures : Textures
    interface Textures {
        operator fun get(assetName : String) = getOrLoadTexture2D(assetName)

        fun getOrLoadTexture2D(assetName: String) : Texture2D

        val defaultTexture2D: Texture2D
    }

    val models : Models
    interface Models {
        operator fun get(assetName: String) = getOrLoadModel(assetName)

        fun getOrLoadModel(assetName: String): Model

        val defaultModel: Model
    }
}