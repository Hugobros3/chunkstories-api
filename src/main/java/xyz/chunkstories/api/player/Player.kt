//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.input.InputsManager
import xyz.chunkstories.api.plugin.commands.CommandEmitter
import java.util.*

data class PlayerID(val uuid: UUID)

interface Player : CommandEmitter {
    override val name: String
    val id: PlayerID
    val displayName: String

    val state: PlayerState

    val inputsManager: InputsManager

    /** Sends a text message to this player */
    override fun sendMessage(message: String)
}

sealed class PlayerState(val player: Player) {
    class Ingame(player: Player, val entity: Entity) : PlayerState(player)
    class Spectating(player: Player, val location: Location) : PlayerState(player)
}

val Player.entityIfIngame: Entity?
    get() = when(val state = this.state) {
        is PlayerState.Ingame -> state.entity
        else -> null
    }