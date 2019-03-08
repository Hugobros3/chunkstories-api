//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server

import xyz.chunkstories.api.player.Player

interface PermissionsManager {
    fun hasPermission(player: Player, permissionNode: String): Boolean
}
