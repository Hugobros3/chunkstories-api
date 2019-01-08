//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin;

import java.util.Collection;

import xyz.chunkstories.api.events.Event;
import xyz.chunkstories.api.events.Listener;
import xyz.chunkstories.api.plugin.commands.Command;
import xyz.chunkstories.api.plugin.commands.CommandEmitter;
import xyz.chunkstories.api.plugin.commands.CommandHandler;
import xyz.chunkstories.api.plugin.commands.SystemCommand;
import xyz.chunkstories.api.util.IterableIterator;

/** The Plugin manager is responsible for loading/unloading of plugins, handling
 * of commands and handling of events */
public interface PluginManager {
	/** (Re)Loads all necessary plugins */
	public void reloadPlugins();

	/** Disables all enable plugins */
	public void disablePlugins();

	/** Lists active plugins */
	public IterableIterator<ChunkStoriesPlugin> activePlugins();

	/** Dispatches an command to the plugins
	 * 
	 * @param emitter Whoever sent it
	 * @param commandName The command name
	 * @param arguments The arguments, splitted by spaces
	 * @return Whether the command executed sucessfully */
	public boolean dispatchCommand(CommandEmitter emitter, String commandName, String[] arguments);

	/** Register a new system command */
	public SystemCommand registerCommand(String commandName, String... aliases);

	/** Assigns a new command handler to a command */
	public void registerCommandHandler(String commandName, CommandHandler commandHandler);

	/** Returns the command matching this name or alias */
	public Command findCommandUsingAlias(String commandName);

	/** Returns a collection with all registered commands */
	public Collection<Command> commands();

	/* Events handling */

	/** Register a Listener in an plugin */
	public void registerEventListener(Listener listener, ChunkStoriesPlugin plugin);

	/** Fires an Event, pass it to all plugins that are listening for this kind of
	 * event */
	public void fireEvent(Event event);
}
