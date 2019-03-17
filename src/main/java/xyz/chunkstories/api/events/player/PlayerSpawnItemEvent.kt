package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.player.Player

abstract class PlayerSpawnItemEvent(val player: Player, val item: Item, val amount: Int) : CancellableEvent()

/** When a player tries to summon an item onto the ground */
class PlayerSpawnItemOnGroundEvent(player: Player, item: Item, amount: Int, val location: Location) : PlayerSpawnItemEvent(player, item, amount) {
    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this
        var listenersStatic = EventListeners(PlayerMoveItemEvent::class.java)
            internal set
    }
}

/** When a player tries to summon an item into an inventory */
class PlayerSpawnItemInInventoryEvent(player: Player, item: Item, amount: Int, val inventory: Inventory, val x: Int, val y: Int) : PlayerSpawnItemEvent(player, item, amount) {
    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this
        var listenersStatic = EventListeners(PlayerMoveItemEvent::class.java)
            internal set
    }
}