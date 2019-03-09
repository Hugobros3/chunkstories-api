//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.events.categories.PlayerEvent
import xyz.chunkstories.api.player.Player

class PlayerLoginEvent(override val player: Player) : CancellableEvent(), PlayerEvent {

    override val listeners: EventListeners
        get() = listenersStatic
    // Specific event code

    var connectionMessage: String? = null
    var refusedConnectionMessage = "Connection was refused by a plugin."

    init {
        this.connectionMessage = "#FFFF00$player joined the server"
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerLoginEvent::class.java)
            internal set
    }
}
