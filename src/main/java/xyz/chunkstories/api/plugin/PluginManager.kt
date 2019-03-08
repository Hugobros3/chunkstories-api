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

/** The Plugin manager is responsible for loading/unloading of plugins, handling
 * of commands and handling of events  */
interface PluginManager {
    /** (Re)Loads all necessary plugins  */
    fun reloadPlugins()

    /** Disables all enable plugins  */
    fun disablePlugins()

    /** Lists active plugins  */
    fun activePlugins(): Collection<ChunkStoriesPlugin>

    /** Dispatches an command to the plugins
     *
     * @param emitter Whoever sent it
     * @param commandName The command name
     * @param arguments The arguments, splitted by spaces
     * @return Whether the command executed sucessfully
     */
    fun dispatchCommand(emitter: CommandEmitter, commandName: String, arguments: Array<String>): Boolean

    /** Register a new system command  */
    fun registerCommand(commandName: String, commandHandler: CommandHandler, vararg aliases: String): Command

    fun unregisterCommand(command: Command)

    ///** Assigns a new command handler to a command  */
    //fun registerCommandHandler(commandName: String, commandHandler: CommandHandler): Command

    /** Returns the command matching this name or alias  */
    fun findCommandUsingAlias(commandName: String): Command?

    /** Returns a collection with all registered commands  */
    fun commands(): Collection<Command>

    /* Events handling */

    /** Register a Listener in an plugin  */
    fun registerEventListener(listener: Listener, plugin: ChunkStoriesPlugin)

    /** Fires an Event, pass it to all plugins that are listening for this kind of
     * event  */
    fun fireEvent(event: Event)

    fun getPluginDirectory(plugin: ChunkStoriesPlugin): File
}
