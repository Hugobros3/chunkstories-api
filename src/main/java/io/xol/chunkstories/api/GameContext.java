package io.xol.chunkstories.api;

import org.slf4j.Logger;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.plugin.PluginManager;
import io.xol.chunkstories.api.workers.Tasks;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface GameContext
{
	public Content getContent();

	/** Accesses the pluginManager */
	public PluginManager getPluginManager();
	
	/** Returns an interface to schedule work on */
	public Tasks tasks();
	
	/** Prints some text, usefull for debug purposes */
	public void print(String message);

	/** Allows for writing to a .log file for debug purposes */
	public Logger logger();
}
