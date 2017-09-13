package io.xol.chunkstories.api.plugin;

import java.util.Collection;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.Listener;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.plugin.commands.SystemCommand;
import io.xol.chunkstories.api.util.IterableIterator;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** The Plugin manager is responsible for loading/unloading of plugins, handling of commands and handling of events */
public interface PluginManager
{
	/** (Re)Loads all necessary plugins */
	public void reloadPlugins();
	
	/** Disables all enable plugins */
	public void disablePlugins();
	
	/** Lists active plugins */
	public IterableIterator<ChunkStoriesPlugin> activePlugins();

	/* Commands management */
	
	/**
	 * Dispatches an command to the plugins
	 * @param emitter Whoever sent it
	 * @param commandName The command name
	 * @param arguments The arguments, splitted by spaces
	 * @return Whether the command executed sucessfully
	 */
	public boolean dispatchCommand(CommandEmitter emitter, String commandName, String[] arguments);
	
	/** Register a new system command */
	public SystemCommand registerCommand(String commandName, String... aliases);
	
	/** Un-register a system command */
	//Is this relevant ?
	//public void unregisterCommand(SystemCommand command);
	
	/** Assigns a new command handler to a command */
	public void registerCommandHandler(String commandName, CommandHandler commandHandler);
	
	/** Returns the command matching this name or alias */
	public Command findCommandUsingAlias(String commandName);
	
	/** Returns a collection with all registered commands */
	public Collection<Command> commands();
	
	/* Events handling */
	
	/**
	 * Register a Listener in an plugin
	 * @param l
	 * @param plugin
	 */
	public void registerEventListener(Listener listener, ChunkStoriesPlugin plugin);
	
	/**
	 * Fires an Event, pass it to all plugins that are listening for this kind of event
	 * @param event
	 */
	public void fireEvent(Event event);
}
