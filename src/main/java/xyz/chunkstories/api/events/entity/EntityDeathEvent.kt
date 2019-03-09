//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.entity

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.EventListeners

/** This event is called upon confirmed death of a living entity. You can't and
 * shouldn't prevent it from dying here, instead use the EntityDamageEvent to
 * cancel the damage.  */
class EntityDeathEvent(entity: Entity) : Event() {

    override val listeners: EventListeners
        get() = listenersStatic

    // Specific event code

    var entity: Entity
        internal set

    init {
        this.entity = entity
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(EntityDeathEvent::class.java)
            internal set
    }
}
