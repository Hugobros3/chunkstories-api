//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.inventory

import org.joml.Vector3d
import xyz.chunkstories.api.entity.EntityDroppedItem
import xyz.chunkstories.api.entity.traits.serializable.TraitRotation
import xyz.chunkstories.api.events.player.PlayerDropItemEvent
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.item.inventory.moveTo
import xyz.chunkstories.api.net.packets.PacketInventoryMoveItemPile
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.entityIfIngame
import xyz.chunkstories.api.world.WorldSub
import xyz.chunkstories.api.world.WorldMaster

/** Logic helper to deal with the insanity of 'virtual' grid slots (ie not mapped to a real inventory slot) */
sealed class InventorySlot {
    abstract val visibleContents: Pair<Item, Int>?

    class RealSlot(val inventory: Inventory, val x: Int, val y: Int) : InventorySlot() {
        val actualItemPile: ItemPile?
            get() = inventory.getItemPileAt(x, y)

        var stolen = 0

        override val visibleContents: Pair<Item, Int>?
            get() {
                val actual = actualItemPile ?: return null
                val remaining = actual.amount - stolen
                return Pair(actual.item, remaining)
            }
    }

    /** For the crafting mechanics: moving the item there doesn't actually move anything, just a visual trick */
    class FakeSlot : InventorySlot() {
        var stealingFrom = mutableListOf<RealSlot>() // n references to the 'stealing' slot denotes we're showing n items from this slot in this one

        override val visibleContents: Pair<Item, Int>?
            get() {
                if (stealingFrom.isEmpty())
                    return null
                else {
                    val total = stealingFrom.size
                    return Pair(stealingFrom[0].actualItemPile!!.item, total)
                }

                //val actual = stealingFrom?.actualItemPile ?: return null
                //return Pair(actual.item, amount)
            }
    }

    abstract class SummoningSlot : InventorySlot() {
        open val nukesItems = false
        abstract fun commitTransfer(destinationInventory: Inventory, destX: Int, destY: Int, amount: Int)
    }
}

fun transfer(from: InventorySlot, to: InventorySlot?, amount: Int, player: Player?) {
    // ensure we can take so many items
    val amountAvailable = from.visibleContents?.second ?: 0
    if (amount > amountAvailable)
        return

    // ensure we can place so many items without overflowing
    if (to != null && to.visibleContents.let { it != null && it.first.definition.maxStackSize < it.second + amount })
        return

    when (from) {
        is InventorySlot.RealSlot -> {
            val sourcePile = from.actualItemPile!!
            when (to) {
                is InventorySlot.RealSlot -> {
                    sourcePile.moveTo(to.inventory, to.x, to.y, amount)
                }
                is InventorySlot.FakeSlot -> {
                    if (to.visibleContents == null || to.visibleContents!!.first.canStackWith(sourcePile.item)) {
                        from.stolen += amount

                        repeat(amount) {
                            to.stealingFrom.add(from)
                        }
                    }
                }
                is InventorySlot.SummoningSlot -> {
                    if(to.nukesItems) {
                        val world = player?.entityIfIngame?.world
                        if (world == null || world is WorldMaster) {
                            sourcePile.amount -= amount
                        } else {
                            //TODO nuke the items via a packet
                        }
                    }
                }
                null -> drop(sourcePile, amount, player)
            }
        }
        is InventorySlot.FakeSlot -> {
            when (to) {
                is InventorySlot.RealSlot -> {
                    repeat(amount) {
                        val realSlot = from.stealingFrom.get(0)
                        from.stealingFrom.removeAt(0)
                        realSlot.stolen--
                        if (realSlot != to) {
                            transfer(realSlot, to, 1, player)
                        }
                    }
                }
                is InventorySlot.FakeSlot -> {
                    repeat(amount) {
                        val realSlot = from.stealingFrom.removeAt(0)
                        to.stealingFrom.add(realSlot)
                    }
                }
                is InventorySlot.SummoningSlot -> {
                    // disallowed, do nothing
                }
                null -> {
                    val dropFrom = mutableMapOf<InventorySlot.RealSlot, Int>()
                    // take amount references away
                    repeat(amount) {
                        val realSlot = from.stealingFrom.get(0)
                        transfer(from, realSlot, 1, player)
                        // count how many objects we're dropping, per slot
                        dropFrom[realSlot] = (dropFrom[realSlot] ?: 0) + 1
                    }

                    // only make one drop per slot
                    for ((realSlot, dropAmount) in dropFrom)
                        transfer(realSlot, null, dropAmount, player)
                }
            }
        }
        is InventorySlot.SummoningSlot -> {
            when (to) {
                is InventorySlot.RealSlot -> {
                    from.commitTransfer(to.inventory, to.x, to.y, amount)
                }
                is InventorySlot.FakeSlot -> {
                    // disallow this
                }
                is InventorySlot.SummoningSlot -> {
                    // disallow this too
                }
                null -> {
                    //TODO do we want this to be allowed?
                }
            }
        }
    }
}

fun drop(itemPile: ItemPile, amount: Int, player: Player?) {
    val playerEntity = player?.entityIfIngame
    val world = playerEntity?.world

    if (world == null) {
        itemPile.amount -= amount
    } else if (world is WorldMaster) {
        // For single player scenarios, this logic is replicated in PacketInventoryMoveItemPile
        // TODO how about not doing that

        val event = PlayerDropItemEvent(player, itemPile, amount, playerEntity.location)
        world.gameInstance.pluginManager.fireEvent(event)
        if (!event.isCancelled) {
            val initialVelocity = playerEntity.traits[TraitRotation::class]?.directionLookingAt?.let { Vector3d(it).mul(0.1).add(0.0, 0.2, 0.0) } ?: Vector3d(0.0)
            EntityDroppedItem.spawn(itemPile.item, amount, playerEntity.location, initialVelocity)
            itemPile.amount -= amount
        }
    } else if (world is WorldSub) {
        // In MP scenario, move into /dev/null
        val packetMove = PacketInventoryMoveItemPile(world, itemPile, itemPile.inventory, null, itemPile.x, itemPile.y, 0, 0, amount)
        world.remoteServer.pushPacket(packetMove)
    }
}