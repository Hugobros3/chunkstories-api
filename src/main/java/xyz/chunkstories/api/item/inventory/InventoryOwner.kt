//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.gui.Layer
import xyz.chunkstories.api.gui.inventory.InventoryManagementUIPanel
import xyz.chunkstories.api.item.Item

/** Returnable by Inventory.getHolder()  */
interface InventoryOwner

interface InventoryCallbacks {
    fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?)

    fun refreshCompleteInventory()

    fun isAccessibleTo(entity: Entity): Boolean

    fun isItemAccepted(item: Item) : Boolean = true

    val inventoryName: String

    /** Override this to have a custom inventory panel ! */
    fun createMainInventoryPanel(inventory: Inventory, layer: Layer) : InventoryManagementUIPanel? = null
}