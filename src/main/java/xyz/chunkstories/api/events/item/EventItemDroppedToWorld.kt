//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.item

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.events.entity.EntityDeathEvent
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile

class EventItemDroppedToWorld(
        // Specific event code

        var location: Location?, var inventoryFrom: Inventory?, var itemPile: ItemPile?) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    /** The entity to be added to the world that will represent this item pile  */
    var itemEntity: Entity? = null

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(EntityDeathEvent::class.java)
            internal set
    }
}
