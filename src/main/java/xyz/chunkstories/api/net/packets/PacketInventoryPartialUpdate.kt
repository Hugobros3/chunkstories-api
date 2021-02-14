//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import java.io.DataInputStream
import java.io.DataOutputStream

import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.net.PacketWorld
import xyz.chunkstories.api.world.World

import xyz.chunkstories.api.item.inventory.obtainItemPileFromStream
import xyz.chunkstories.api.item.inventory.saveIntoStream
import xyz.chunkstories.api.player.Player

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

    override fun send(dos: DataOutputStream) {
        writeInventoryHandle(dos, inventory)

        dos.writeInt(x)
        dos.writeInt(y)

        if (itemPile == null)
            dos.writeInt(0)
        else {
            itemPile!!.saveIntoStream(world.gameInstance.contentTranslator, dos)
        }
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        inventory = obtainInventoryByHandle(dis, world)

        val x = dis.readInt()
        val y = dis.readInt()

        val (item, amount) =
        try {
            obtainItemPileFromStream(world.gameInstance.contentTranslator, dis)
        } catch (e: NullItemException) {
            // This is sane behavior !
            Pair(null, null)
        } catch (e: UndefinedItemTypeException) {
            // This is slightly more problematic.
            world.logger.error("Undefined item: ", e)
            throw e
        }

        if (inventory != null)
            inventory!!.setItemAt(x, y, item, amount ?: 0, true)
    }

}
