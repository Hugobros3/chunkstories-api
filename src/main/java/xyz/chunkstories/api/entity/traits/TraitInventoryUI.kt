package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.gui.inventory.InventoryUI

abstract class TraitInventoryUI(entity: Entity) : Trait(entity) {

    /** Entities wishing to have a custom inventory panel to show up when the inventory is brought up can use this trait to do so */
    abstract fun createMainInventoryPanel() : InventoryUI
}