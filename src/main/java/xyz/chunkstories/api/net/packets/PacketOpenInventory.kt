//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.client.net.ClientPacketsProcessor
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.exceptions.PacketProcessingException
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.obtainInventoryByHandle
import xyz.chunkstories.api.item.inventory.writeInventoryHandle
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class PacketOpenInventory : PacketWorld {
    protected var inventory: Inventory? = null

    @Suppress("unused")
    constructor(world: World) : super(world)

    @Suppress("unused")
    constructor(world: World, inventory: Inventory) : super(world) {
        this.inventory = inventory
    }

    @Throws(IOException::class)
    override fun send(destinator: PacketDestinator, out: DataOutputStream, context: PacketSendingContext) {
        writeInventoryHandle(out, inventory)
    }

    @Throws(IOException::class, PacketProcessingException::class)
    override fun process(sender: PacketSender, dis: DataInputStream, processor: PacketReceptionContext) {
        val inventory = obtainInventoryByHandle(dis, processor) ?: error("Can't find inventory to open!")

        if (processor is ClientPacketsProcessor) {
            val client = processor.context
            val currentControlledEntity = client.player.controlledEntity ?: return

            val ownInventory = currentControlledEntity.traits[TraitInventory::class]?.inventory//currentControlledEntity?.traits?.tryWith<TraitInventory, Inventory>(TraitInventory::class.java, ReturnsAction<TraitInventory, Inventory> { it.getInventory() })

            if (ownInventory != null)
                client.gui.openInventories(ownInventory, inventory)
            else
                client.gui.openInventories(inventory)
        }
    }

}
