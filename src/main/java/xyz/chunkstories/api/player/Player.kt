//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
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

sealed class PlayerState {
    object None : PlayerState()
    class Ingame(val entity: Entity) : PlayerState() {
        override val location: Location?
            get() = entity.location
    }
    class Spectating(override var location: Location) : PlayerState()

    open val location: Location?
        get() = null
}

val Player.entityIfIngame: Entity?
    get() = when(val state = this.state) {
        is PlayerState.Ingame -> state.entity
        else -> null
    }