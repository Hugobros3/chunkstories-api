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
import xyz.chunkstories.api.plugin.commands.CommandEmitter
import java.util.*

data class PlayerID(val uuid: UUID)

/** Represents a player (Remote or Local) as the once calling shots */
interface Player : CommandEmitter {
    override val name: String
    val displayName: String

    /** Sends a text message to this player */
    override fun sendMessage(msg: String)
}

/** A player currently acting as a certain entity */
interface IngamePlayer : Player, Controller, Subscriber {
    val entity: Entity
}

/** A player simply spectating a map */
interface SpectatingPlayer : Player, Subscriber {
    val location: Location
}