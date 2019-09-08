//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.components

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.InventoryCallbacks
import xyz.chunkstories.api.item.inventory.InventoryOwner
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate
import xyz.chunkstories.api.server.RemotePlayer
import xyz.chunkstories.api.world.WorldUser
import xyz.chunkstories.api.world.cell.CellComponents
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

class VoxelInventoryComponent(cell: CellComponents, width: Int, height: Int) : VoxelComponent(cell), InventoryOwner, InventoryCallbacks {
    val inventory: Inventory

    override val inventoryName: String
        get() = holder.cell.voxel.name

    init {
        this.inventory = Inventory(width, height, this, this)
    }

    override fun refreshCompleteInventory() {
        this@VoxelInventoryComponent.pushComponentEveryone()
    }

    override fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?) {
        for (user in holder.cell.chunk.holder.users) {
            if (user is RemotePlayer) {
                val packet = PacketInventoryPartialUpdate(holder.cell.world, inventory, x, y, pileChanged)
                user.pushPacket(packet)
            }
        }
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        inventory.saveToStream(dos, holder.world.contentTranslator)
        //inventory.pushInventory(destinator, dos, getHolder().getWorld().getContentTranslator());
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        inventory.loadFromStream(dis, holder.world.contentTranslator)
    }

    override fun isAccessibleTo(entity: Entity): Boolean {
        //TODO compute distance ?
        return true
    }

    override fun isItemAccepted(item: Item): Boolean {
        return true
    }
}
