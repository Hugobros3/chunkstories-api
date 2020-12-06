//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.Listener
import xyz.chunkstories.api.plugin.commands.Command
import xyz.chunkstories.api.plugin.commands.CommandEmitter
import xyz.chunkstories.api.plugin.commands.CommandHandler
import java.io.File

interface PluginManager {
    fun reloadPlugins()
    val activePlugins: List<Plugin>

    fun registerCommand(commandName: String, commandHandler: CommandHandler, vararg aliases: String): Command
    val commands: List<Command>
    fun findCommandUsingAlias(commandName: String): Command?
    fun dispatchCommand(emitter: CommandEmitter, commandName: String, arguments: Array<String>): Boolean

    fun registerEventListener(listener: Listener, plugin: Plugin)
    fun fireEvent(event: Event)

    // TODO ideally should be more secure
    fun getPluginDirectory(plugin: Plugin): File
}
