//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import io.xol.chunkstories.api.server.Server;

/** A type of plugin that exclusivly runs on the server/master */
public abstract class ServerPlugin extends ChunkStoriesPlugin {
	private final Server serverInterface;

	public ServerPlugin(PluginInformation pluginInformation, Server Client) {
		super(pluginInformation, Client);
		this.serverInterface = Client;
	}

	public Server getServer() {
		return serverInterface;
	}
}
