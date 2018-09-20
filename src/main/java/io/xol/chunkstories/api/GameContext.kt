//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api

import org.slf4j.Logger

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.plugin.PluginManager
import io.xol.chunkstories.api.workers.Tasks

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
