//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server

import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.PlayerID
import xyz.chunkstories.api.world.GameInstance

interface Host : GameInstance {
    val players: Set<Player>

    var permissionsManager: PermissionsManager

    fun getPlayer(name: String): Player?
    fun getPlayer(id: PlayerID): Player?

    fun Player.disconnect(disconnectMessage: String)

    fun broadcastMessage(message: String)

    @Deprecated("TODO move to implementation")
    fun reloadConfig()
}
