//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api

import org.slf4j.Logger

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.plugin.PluginManager
import xyz.chunkstories.api.workers.Tasks

interface GameContext {
    val content: Content

    /** Accesses the pluginManager  */
    val pluginManager: PluginManager

    /** Returns an interface to schedule work on  */
    val tasks: Tasks

    /** Prints some text, usefull for debug purposes  */
    fun print(message: String)

    /** Allows for writing to a .log file for debug purposes  */
    fun logger(): Logger
}
