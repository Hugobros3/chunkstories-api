//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import xyz.chunkstories.api.graphics.rendergraph.Pass
import xyz.chunkstories.api.graphics.rendergraph.RegisteredDrawingSystem
import xyz.chunkstories.api.graphics.rendergraph.RenderGraph
import xyz.chunkstories.api.graphics.representation.Model
import xyz.chunkstories.api.graphics.systems.dispatching.DispatchingSystem
import xyz.chunkstories.api.graphics.systems.drawing.DrawingSystem

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

    /** Dispatching systems: Register them here! */
    val dispatchingSystems: Set<DispatchingSystem>

    val backend: GraphicsBackend
}

/** When creating systems implementations you will need one of those.
 * No specifics as we don't assume anything about graphical APIs or game implementations !*/
interface GraphicsBackend {
    fun createDrawingSystem(pass: Pass, registeredDrawingSystem: RegisteredDrawingSystem) : DrawingSystem
}