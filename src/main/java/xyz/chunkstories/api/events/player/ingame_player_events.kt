package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player

class PlayerInputPressedEvent(val player: Player, val input: Input) : CancellableEvent()
class PlayerInputReleasedEvent(val player: Player, val input: Input) : Event()

class PlayerDeathEvent(val player: Player) : Event() {
    var deathMessage: String = player.displayName + " died."
}

/** When a player tries to drop an item on the ground */
class PlayerDropItemEvent(val player: Player, val source: ItemPile, val amount: Int, val location: Location) : CancellableEvent()