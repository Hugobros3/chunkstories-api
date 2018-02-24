//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin.commands;

/**
 * Can represent the server console, a server player, the local client in case of local plugins, and so on...
 */
public interface CommandEmitter
{
	public String getName();

	public void sendMessage(String msg);

	public boolean hasPermission(String permissionNode);
}
