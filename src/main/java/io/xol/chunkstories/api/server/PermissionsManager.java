//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.server;

import io.xol.chunkstories.api.player.Player;

public interface PermissionsManager
{
	public boolean hasPermission(Player player, String permissionNode);
}
