//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin.commands;

import io.xol.chunkstories.api.plugin.PluginInformation;

/** Describes a command installed by a plugin */
public class PluginCommand extends Command {

	private final PluginInformation plugin;

	public PluginCommand(PluginInformation plugin, String commandName) {
		super(commandName);
		this.plugin = plugin;
	}
	
	public PluginInformation getPlugin()
	{
		return plugin;
	}

}
