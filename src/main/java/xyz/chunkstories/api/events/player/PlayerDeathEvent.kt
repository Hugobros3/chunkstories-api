//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.player.Player

class PlayerDeathEvent(
        // Specific event code

        val player: Player) : Event() {

    override val listeners: EventListeners
        get() = listenersStatic
    var deathMessage: String

    init {
        this.deathMessage = player.displayName + " died."
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerDeathEvent::class.java)
            internal set
    }
}
