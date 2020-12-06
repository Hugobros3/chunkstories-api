//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

data class PluginInformation(
        val name: String,
        val version: String = "1.0",
        val authors: String,
        val pluginType: PluginType = PluginType.UNIVERSAL,
        val entryPoint: String
) {

    enum class PluginType {
        UNIVERSAL, CLIENT_ONLY, MASTER
    }
}