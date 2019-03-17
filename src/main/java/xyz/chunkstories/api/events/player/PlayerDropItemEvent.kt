package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player

/** When a player tries to drop an item on the ground */
class PlayerDropItemEvent(val player: Player, val source: ItemPile, val amount: Int, val location: Location) : CancellableEvent() {
    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this
        var listenersStatic = EventListeners(PlayerMoveItemEvent::class.java)
            internal set
    }
}