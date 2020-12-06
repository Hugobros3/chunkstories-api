//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

import java.io.File

import xyz.chunkstories.api.world.GameInstance

// TODO remove plugins altogether and consolidate their functionality into mods instead
abstract class Plugin(val information: PluginInformation, val gameInstance: GameInstance) {
    val directory: File = gameInstance.pluginManager.getPluginDirectory(this)

    val name: String
        get() = information.name

    // Use overridden constructor instead
    //abstract fun onEnable()

    abstract fun onDisable()
}
