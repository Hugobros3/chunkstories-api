//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin;

import xyz.chunkstories.api.client.Client;

public interface ClientPluginManager extends PluginManager {
	public Client getClient();
}
