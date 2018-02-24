//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import io.xol.chunkstories.api.server.ServerInterface;

public interface ServerPluginManager extends PluginManager
{
	public ServerInterface getServerInterface();
}
