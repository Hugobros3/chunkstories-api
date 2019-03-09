//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.events.categories.PlayerEvent
import xyz.chunkstories.api.player.Player

class PlayerLogoutEvent(
        // Specific event code

        override val player: Player) : Event(), PlayerEvent {

    override val listeners: EventListeners
        get() = listenersStatic
    var logoutMessage: String? = null

    init {
        this.logoutMessage = "#FFFF00$player left the server"
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerLogoutEvent::class.java)
            internal set
    }
}
