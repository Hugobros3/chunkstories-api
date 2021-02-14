//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player

/** Describe a player moving item action on Master  */
class PlayerMoveItemEvent(val player: Player, val sourcePile: ItemPile, val targetInventory: Inventory, val targetX: Int, val targetY: Int, val amount: Int) : CancellableEvent()

class PlayerSelectItemEvent(var player: Player, var entity: Entity, var newSlot: Int) : Event()

abstract class PlayerSpawnItemEvent(val player: Player, val item: Item, val amount: Int) : CancellableEvent()

/** When a player tries to summon an item onto the ground */
class PlayerSpawnItemOnGroundEvent(player: Player, item: Item, amount: Int, val location: Location) : PlayerSpawnItemEvent(player, item, amount)

/** When a player tries to summon an item into an inventory */
class PlayerSpawnItemInInventoryEvent(player: Player, item: Item, amount: Int, val inventory: Inventory, val x: Int, val y: Int) : PlayerSpawnItemEvent(player, item, amount)