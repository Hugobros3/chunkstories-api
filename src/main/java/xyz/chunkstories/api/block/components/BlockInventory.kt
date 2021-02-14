//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.block.components

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate
import xyz.chunkstories.api.block.BlockAdditionalData
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.chunk.ChunkCell

open class BlockInventory(cell: ChunkCell, width: Int, height: Int) : BlockAdditionalData(cell), InventoryOwner, InventoryCallbacks {
    val inventory: Inventory = Inventory(width, height, this, this)

    override val inventoryName: String
        get() = cell.data.blockType.name

    override fun refreshCompleteInventory() {
        (cell.world as? WorldMaster)?.apply {
            this@BlockInventory.pushChanges()
        }
    }

    override fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?) {
        (cell.world as? WorldMaster)?.apply {
            for (user in cell.chunk.holder.users) {
                if (user is Player) {
                    val packet = PacketInventoryPartialUpdate(cell.world, inventory, x, y, pileChanged)
                    user.pushPacket(packet)
                }
            }
        }
    }

    override fun isAccessibleTo(entity: Entity): Boolean {
        //TODO compute distance ?
        return entity.location.distance(cell.location) < 6.0F
    }

    override fun isItemAccepted(item: Item): Boolean {
        return true
    }

    override fun deserialize(json: Json) {
        InventorySerialization.deserializeInventory(inventory, cell.world.gameInstance.contentTranslator, json.asDict ?: return)
    }

    override fun serialize(): Json? {
        return InventorySerialization.serializeInventory(inventory, cell.world.gameInstance.contentTranslator, false)
    }
}
