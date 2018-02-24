//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import io.xol.chunkstories.api.client.ClientInterface;

/** A type of plugin that exclusivly runs on the client */
public abstract class ClientPlugin extends ChunkStoriesPlugin
{
	private final ClientInterface clientInterface;

	public ClientPlugin(PluginInformation pluginInformation, ClientInterface clientInterface)
	{
		super(pluginInformation, clientInterface);
		this.clientInterface = clientInterface;
	}

	public ClientInterface getClientInterface()
	{
		return clientInterface;
	}
}
