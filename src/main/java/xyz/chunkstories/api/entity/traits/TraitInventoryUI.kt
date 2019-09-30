//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.gui.inventory.InventoryManagementUIPanel

abstract class TraitInventoryUI(entity: Entity) : Trait(entity) {
    override val traitName = "inventoryUI"

    /** Entities wishing to have a custom inventory panel to show up when the inventory is brought up can use this trait to do so */
    abstract fun createMainInventoryPanel() : InventoryManagementUIPanel
}