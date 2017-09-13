package io.xol.chunkstories.api.plugin.commands;

import io.xol.chunkstories.api.plugin.PluginInformation;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

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
