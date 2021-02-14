//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import org.joml.Vector3d
import xyz.chunkstories.api.entity.EntityDroppedItem
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.entity.traits.serializable.TraitRotation
import xyz.chunkstories.api.events.player.PlayerDropItemEvent
import xyz.chunkstories.api.events.player.PlayerMoveItemEvent
import xyz.chunkstories.api.events.player.PlayerSpawnItemInInventoryEvent
import xyz.chunkstories.api.events.player.PlayerSpawnItemOnGroundEvent
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.entityIfIngame
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream

class PacketInventoryMoveItemPile : PacketWorld {
    private lateinit var itemPile: ItemPile
    private var sourceInventory: Inventory? = null
    private var destinationInventory: Inventory? = null
    private var sourceX: Int = 0
    private var sourceY: Int = 0
    private var destX: Int = 0
    private var destY: Int = 0
    private var amount: Int = 0
    private var destroy: Boolean = false

    constructor(world: World) : super(world) {}

    constructor(world: World, itemPile: ItemPile, from: Inventory?, to: Inventory?, oldX: Int, oldY: Int, newX: Int, newY: Int, amount: Int) : super(world) {
        this.itemPile = itemPile
        this.sourceInventory = from
        this.destinationInventory = to
        this.sourceX = oldX
        this.sourceY = oldY
        this.destX = newX
        this.destY = newY
        this.amount = amount
    }

    override fun send(dos: DataOutputStream) {
        // Describe the move
        dos.writeInt(sourceX)
        dos.writeInt(sourceY)
        dos.writeInt(destX)
        dos.writeInt(destY)

        dos.writeBoolean(destroy)
        dos.writeInt(amount)

        // Describe the inventories
        writeInventoryHandle(dos, sourceInventory)
        writeInventoryHandle(dos, destinationInventory)

        // Describe the itemPile if we are trying to spawn an item from nowhere
        if (sourceInventory == null || sourceInventory!!.owner == null) {
            //itemPile!!.item.save(stream)
            itemPile.saveIntoStream(world.gameInstance.contentTranslator, dos)
        }
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        val playerEntity = player?.entityIfIngame

        if (playerEntity == null) {
            world.logger.error("Received a move item packet from a player without a controlled entity, ignoring.")
            return
        }

        val world = playerEntity.world

        val sourceX = dis.readInt()
        val sourceY = dis.readInt()
        val destX = dis.readInt()
        val destY = dis.readInt()

        val destroy = dis.readBoolean()
        val amount = dis.readInt()

        val sourceInventory = obtainInventoryByHandle(dis, world)
        val destinationInventory = obtainInventoryByHandle(dis, world)

        var item: Item? = null

        // If this pile is spawned from the void
        if (sourceInventory == null) {
            try {
                item = obtainItemPileFromStream(world.gameInstance.contentTranslator, dis).first
            } catch (e: NullItemException) {
                world.logger.info("$player is trying to spawn a null ItemPile for some reason.")
            } catch (e: UndefinedItemTypeException) {
                world.logger.warn(e.message)
            }

        } else {
            item = sourceInventory.getItemPileAt(sourceX, sourceY)?.item
        }

        if (sourceInventory?.isAccessibleTo(playerEntity) == false) {
            player.sendMessage("You don't have access to the source inventory.")
            return
        }

        if (destinationInventory?.isAccessibleTo(playerEntity) == false) {
            player.sendMessage("You don't have access to the destination inventory.")
            return
        }

        val sourceItemPile = sourceInventory?.getItemPileAt(sourceX, sourceY)

        val event = when {
            sourceItemPile != null -> when {
                destinationInventory != null -> PlayerMoveItemEvent(player, sourceItemPile, destinationInventory, destX, destY, amount)
                else -> PlayerDropItemEvent(player, sourceItemPile, amount, playerEntity.location)
            }
            else -> when {
                destinationInventory != null -> PlayerSpawnItemInInventoryEvent(player, item!!, amount, destinationInventory, destX, destY)
                else -> PlayerSpawnItemOnGroundEvent(player, item!!, amount, playerEntity.location)
            }
        }

        world.gameInstance.pluginManager.fireEvent(event)

        if (!event.isCancelled) {
            // Restrict item spawning
            if (sourceInventory == null) {
                // player.sendMessage("Notice : dragging stuff from /dev/null to your inventory
                // should be limited by permission.");

                if (player.hasPermission("items.spawn") || playerEntity.traits[TraitCreativeMode::class]?.enabled == true) {
                    // Let it happen when in creative mode or owns items.spawn perm
                } else {
                    player.sendMessage("#C00000You are neither in creative mode nor have the items.spawn permission.")
                    return
                }
            }

            // If target inventory is null, this means the item was dropped
            if (destinationInventory == null) {
                // TODO this really needs some kind of permissions system
                // TODO or not ? Maybe the cancellable event deal can prevent this

                // If we're pulling this out of an inventory ( and not /dev/null ), we need to
                sourceItemPile?.let { it.amount -= amount }

                if (!destroy) {
                    val initialVelocity = playerEntity.traits[TraitRotation::class]?.directionLookingAt?.let { Vector3d(it).mul(0.1).add(0.0, 0.2, 0.0) } ?: Vector3d(0.0)
                    EntityDroppedItem.spawn(item!!, amount, playerEntity.location, initialVelocity)
                }
            } else {
                if (sourceItemPile != null) {
                    sourceItemPile.moveTo(destinationInventory, destX, destY, amount)
                } else {
                    destinationInventory.placeItemAt(destX, destY, item!!, amount)
                }
            }
        }
    }
}
