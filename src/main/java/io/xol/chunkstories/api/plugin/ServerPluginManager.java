//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import io.xol.chunkstories.api.server.Server;

public interface ServerPluginManager extends PluginManager {
	public Server getServer();
}
