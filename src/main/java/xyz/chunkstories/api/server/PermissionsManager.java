//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server;

import xyz.chunkstories.api.player.Player;

public interface PermissionsManager {
	public boolean hasPermission(Player player, String permissionNode);
}
