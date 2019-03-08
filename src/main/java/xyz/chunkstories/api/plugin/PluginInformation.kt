//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

import java.io.File

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.exceptions.plugins.PluginCreationException

data class PluginInformation(
        val name: String,
        val pluginVersion: String = "1.0",
        val author: String,
        val pluginType: PluginType = PluginType.UNIVERSAL,
        val entryPoint: String
) {

    enum class PluginType {
        UNIVERSAL, CLIENT_ONLY, MASTER
    }
}