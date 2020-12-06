//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.PacketProcessingException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.net.PacketSendingContext
import xyz.chunkstories.api.net.PacketWorld
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.net.PacketReceptionContext

import xyz.chunkstories.api.item.inventory.obtainItemPileFromStream
import xyz.chunkstories.api.item.inventory.saveIntoStream

class PacketInventoryPartialUpdate : PacketWorld {
    private var inventory: Inventory? = null

    private var x: Int = 0
    private var y: Int = 0

    private var itemPile: ItemPile? = null

    constructor(world: World) : super(world)

    constructor(world: World, inventory: Inventory?, x: Int, y: Int, newItemPile: ItemPile?) : super(world) {
        this.inventory = inventory
        this.x = x
        this.y = y
        this.itemPile = newItemPile
    }

    @Throws(IOException::class)
    override fun send(destinator: PacketDestinator, out: DataOutputStream, context: PacketSendingContext) {
        writeInventoryHandle(out, inventory)

        out.writeInt(x)
        out.writeInt(y)

        if (itemPile == null)
            out.writeInt(0)
        else {
            itemPile!!.saveIntoStream(world.contentTranslator, out)
        }
    }

    @Throws(IOException::class, PacketProcessingException::class)
    override fun process(sender: PacketSender, stream: DataInputStream, processor: PacketReceptionContext) {
        inventory = obtainInventoryByHandle(stream, processor)

        val x = stream.readInt()
        val y = stream.readInt()

        val (item, amount) =
        try {
            obtainItemPileFromStream(processor.world!!.contentTranslator, stream)
        } catch (e: NullItemException) {
            // This is sane behavior !
            Pair(null, null)
        } catch (e: UndefinedItemTypeException) {
            // This is slightly more problematic.
            processor.logger().error("Undefined item: ", e)
            throw e
        }

        if (inventory != null)
            inventory!!.setItemAt(x, y, item, amount ?: 0, true)
    }

}
