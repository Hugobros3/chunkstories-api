//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityGroundItem
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.events.player.PlayerDropItemEvent
import xyz.chunkstories.api.events.player.PlayerMoveItemEvent
import xyz.chunkstories.api.events.player.PlayerSpawnItemInInventoryEvent
import xyz.chunkstories.api.events.player.PlayerSpawnItemOnGroundEvent
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.server.ServerPacketsProcessor.ServerPlayerPacketsProcessor
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class PacketInventoryMoveItemPile : PacketWorld {
    private var itemPile: ItemPile? = null
    private var sourceInventory: Inventory? = null
    private var destinationInventory: Inventory? = null
    private var sourceX: Int = 0
    private var sourceY: Int = 0
    private var destX: Int = 0
    private var destY: Int = 0
    private var amount: Int = 0

    constructor(world: World) : super(world) {}

    constructor(world: World, itemPile: ItemPile, from: Inventory, to: Inventory?, oldX: Int, oldY: Int, newX: Int, newY: Int, amount: Int) : super(world) {
        this.itemPile = itemPile
        this.sourceInventory = from
        this.destinationInventory = to
        this.sourceX = oldX
        this.sourceY = oldY
        this.destX = newX
        this.destY = newY
        this.amount = amount
    }

    @Throws(IOException::class)
    override fun send(destinator: PacketDestinator, stream: DataOutputStream, context: PacketSendingContext) {
        // Describe the move
        stream.writeInt(sourceX)
        stream.writeInt(sourceY)
        stream.writeInt(destX)
        stream.writeInt(destY)

        stream.writeInt(amount)

        // Describe the inventories
        writeInventoryHandle(stream, sourceInventory)
        writeInventoryHandle(stream, destinationInventory)

        // Describe the itemPile if we are trying to spawn an item from nowhere
        if (sourceInventory == null || sourceInventory!!.owner == null) {
            //itemPile!!.item.save(stream)
            itemPile!!.saveIntoStream(context.world.contentTranslator, stream)
        }
    }

    @Throws(IOException::class)
    override fun process(sender: PacketSender, stream: DataInputStream, processor: PacketReceptionContext) {
        if (processor !is ServerPlayerPacketsProcessor) {
            processor.logger().warn(
                    "Received a " + this.javaClass.simpleName + " but this GameContext isn't providen with a packet processor made to deal with it")
            return
        }

        val player = processor.player
        val playerEntity = player.controlledEntity

        if (playerEntity == null) {
            processor.logger().error("Received a move item packet from a player without a controlled entity, ignoring.")
            return
        }

        val world = playerEntity.world

        val sourceX = stream.readInt()
        val sourceY = stream.readInt()
        val destX = stream.readInt()
        val destY = stream.readInt()

        val amount = stream.readInt()

        val sourceInventory = obtainInventoryByHandle(stream, processor)
        val destinationInventory = obtainInventoryByHandle(stream, processor)

        var item: Item? = null

        // If this pile is spawned from the void
        if (sourceInventory == null) {
            try {
                item = obtainItemPileFromStream(world.contentTranslator, stream).first
            } catch (e: NullItemException) {
                // This ... isn't supposed to happen
                processor.logger().info("User $sender is trying to spawn a null ItemPile for some reason.")
            } catch (e: UndefinedItemTypeException) {
                // This is slightly more problematic
                processor.logger().warn(e.message)
                // e.printStackTrace(processor.getLogger().getPrintWriter());
            }

        } else {
            item = sourceInventory.getItemPileAt(sourceX, sourceY)?.item
        }

        // Check access
        if (sourceInventory?.isAccessibleTo(playerEntity) == false) {
            player.sendMessage("You don't have access to the source inventory.")
            return
        }

        if (destinationInventory?.isAccessibleTo(playerEntity) == false) {
            player.sendMessage("You don't have access to the destination inventory.")
            return
        }

        val sourceItemPile = sourceInventory?.getItemPileAt(sourceX, sourceY)
        val entity: Entity by lazy { player.controlledEntity ?: throw Exception("You need to be controlling an entity to drop an item to the world !") }

        // Check using event
        val event = when {
            sourceItemPile != null -> when {
                destinationInventory != null -> PlayerMoveItemEvent(player, sourceItemPile, destinationInventory, destX, destY, amount)
                else -> PlayerDropItemEvent(player, sourceItemPile, amount, entity.location)
            }
            else -> when {
                destinationInventory != null -> PlayerSpawnItemInInventoryEvent(player, item!!, amount, destinationInventory, destX, destY)
                else -> PlayerSpawnItemOnGroundEvent(player, item!!, amount, entity.location)
            }
        }
        world.gameContext.pluginManager.fireEvent(event)

        if (!event.isCancelled) {
            // Restrict item spawning
            if (sourceInventory == null) {
                // player.sendMessage("Notice : dragging stuff from /dev/null to your inventory
                // should be limited by permission.");

                if (player.hasPermission("items.spawn") || playerEntity.traits[TraitCreativeMode::class]?.get() == true) {
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
                // remove it from that
                //Inventory sourceInventory = itemPile.getInventory();

                val loc = playerEntity.location
                sourceItemPile?.let { it.amount -= amount }

                val entity = world.content.entities().getEntityDefinition("groundItem")!!.newEntity<EntityGroundItem>(world)
                entity.traits[TraitInventory::class]?.inventory?.addItem(item!!, amount)
                loc.world.addEntity(entity)
                return
            }

            if(sourceItemPile != null)
                sourceItemPile.moveTo(destinationInventory, destX, destY, amount)
            else
                destinationInventory.placeItemAt(destX, destY, item!!, amount)

            //itemPile!!.moveItemPileTo(destinationInventory, destX, destY, amount)
        }
    }
}
