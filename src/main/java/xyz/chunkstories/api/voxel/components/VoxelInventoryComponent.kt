//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.components

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate
import xyz.chunkstories.api.server.RemotePlayer

open class VoxelInventoryComponent(cell: CellComponents, width: Int, height: Int) : VoxelComponent(cell), InventoryOwner, InventoryCallbacks {
    val inventory: Inventory = Inventory(width, height, this, this)

    override val inventoryName: String
        get() = holder.cell.voxel.name

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

    override fun isAccessibleTo(entity: Entity): Boolean {
        //TODO compute distance ?
        return entity.location.distance(holder.cell.location) < 6.0F
    }

    override fun isItemAccepted(item: Item): Boolean {
        return true
    }

    override fun deserialize(json: Json) {
        InventorySerialization.deserializeInventory(inventory, holder.cell.world.contentTranslator, json.asDict ?: return)
    }

    override fun serialize(): Json? {
        return InventorySerialization.serializeInventory(inventory, holder.cell.world.contentTranslator, false)
    }
}
