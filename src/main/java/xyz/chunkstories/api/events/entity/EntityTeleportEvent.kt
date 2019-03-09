//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.entity

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners

class EntityTeleportEvent(
        // Specific event code

        val entity: Entity, val newLocation: Location) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(EntityDamageEvent::class.java)
            internal set
    }
}
