//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.entity

import xyz.chunkstories.api.entity.DamageCause
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners

/** When a EntityLiving damage() method is called  */
class EntityDamageEvent(
        // Specific event code

        val entity: Entity, val damageCause: DamageCause, var damageDealt: Float) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(EntityDamageEvent::class.java)
            internal set
    }
}
