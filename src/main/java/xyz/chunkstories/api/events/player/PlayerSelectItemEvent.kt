//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.player.Player

class PlayerSelectItemEvent(
        // Specific event code

        var player: Player, var entity: Entity, var newSlot: Int) : Event() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerSelectItemEvent::class.java)
            internal set
    }

}
