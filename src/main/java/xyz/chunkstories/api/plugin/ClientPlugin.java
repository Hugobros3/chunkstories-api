//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin;

import xyz.chunkstories.api.client.Client;
import xyz.chunkstories.api.client.IngameClient;

/** A type of plugin that exclusivly runs on the client */
public abstract class ClientPlugin extends ChunkStoriesPlugin {
	private final IngameClient Client;

	public ClientPlugin(PluginInformation pluginInformation, IngameClient Client) {
		super(pluginInformation, Client);
		this.Client = Client;
	}

	public IngameClient getClient() {
		return Client;
	}
}
