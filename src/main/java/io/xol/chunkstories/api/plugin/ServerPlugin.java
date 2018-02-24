//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import io.xol.chunkstories.api.server.ServerInterface;

/** A type of plugin that exclusivly runs on the server/master */
public abstract class ServerPlugin extends ChunkStoriesPlugin
{
	private final ServerInterface serverInterface;

	public ServerPlugin(PluginInformation pluginInformation, ServerInterface clientInterface)
	{
		super(pluginInformation, clientInterface);
		this.serverInterface = clientInterface;
	}

	public ServerInterface getServer()
	{
		return serverInterface;
	}
}
