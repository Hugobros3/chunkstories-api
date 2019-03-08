//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

import java.io.File

import xyz.chunkstories.api.GameContext

abstract class ChunkStoriesPlugin(val pluginInformation: PluginInformation, val gameContext: GameContext) {

    val pluginManager: PluginManager
        get() = gameContext.pluginManager

    val directory: File = pluginManager.getPluginDirectory(this)

    val name: String
        get() = pluginInformation.name

    abstract fun onEnable()

    abstract fun onDisable()
}
