//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster

interface Server : GameContext {
    /** Returns the (public) IP the server is joinable at  */
    val publicIp: String

    /** Returns how many seconds this server has been running for  */
    val uptime: Long

    /** @return The players that are **logged in**
     */
    val connectedPlayers: Set<Player>

    val connectedPlayersCount: Int

    /** Obtains the current permissions manager  */
    /** Installs a custom permissions manager  */
    var permissionsManager: PermissionsManager

    val world: WorldMaster

    fun getPlayerByName(string: String): Player?

    fun getPlayerByUUID(UUID: Long): Player?

    fun broadcastMessage(message: String)

    fun reloadConfig()
}
