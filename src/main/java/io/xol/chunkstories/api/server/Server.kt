//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.server

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.player.Player
import io.xol.chunkstories.api.plugin.PluginManager
import io.xol.chunkstories.api.plugin.ServerPluginManager
import io.xol.chunkstories.api.util.IterableIterator
import io.xol.chunkstories.api.world.WorldMaster

interface Server : GameContext {
    /** Returns the (public) IP the server is joinable at  */
    val publicIp: String

    /** Returns how many seconds this server has been running for  */
    val uptime: Long

    val userPrivileges: UserPrivileges

    /** @return The players that are **logged in**
     */
    val connectedPlayers: IterableIterator<Player>

    val connectedPlayersCount: Int

    override val pluginManager: ServerPluginManager

    /** Obtains the current permissions manager  */
    /** Installs a custom permissions manager  */
    var permissionsManager: PermissionsManager

    val world: WorldMaster

    fun getPlayerByName(string: String): Player?

    fun getPlayerByUUID(UUID: Long): Player?

    fun broadcastMessage(message: String)

    fun reloadConfig()
}
