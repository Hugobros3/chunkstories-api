//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player

/** Describe a player moving item action on Master  */
class PlayerMoveItemEvent(val player: Player, val sourcePile: ItemPile, val targetInventory: Inventory, val targetX: Int, val targetY: Int, val amount: Int) : CancellableEvent() {
    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this
        var listenersStatic = EventListeners(PlayerMoveItemEvent::class.java)
            internal set
    }

}