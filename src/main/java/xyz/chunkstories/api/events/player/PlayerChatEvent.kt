//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.player.Player

class PlayerChatEvent(val player: Player, val message: String) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic
    var formattedMessage: String

    init {

        this.formattedMessage = player.displayName + " > " + message
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerChatEvent::class.java)
            internal set
    }
}
