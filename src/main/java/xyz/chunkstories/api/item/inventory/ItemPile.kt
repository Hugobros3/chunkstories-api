//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.item.Item
import kotlin.concurrent.write

/** A tangible pile of items in an inventory  */
class ItemPile(val inventory: Inventory, val x: Int, val y: Int, val item: Item, amount: Int = 1) {
    var amount = amount
        set(value) {
            inventory.lock.write {
                field = value

                // ItemPiles are smart enough to self-delete when they become == 0 !
                if (value <= 0) {
                    inventory.grid[x][y] = null
                    inventory.refreshItemSlot(x, y, null)
                } else {
                    inventory.refreshItemSlot(x, y, this)
                }
            }
        }

    fun canStackWith(itemPile: ItemPile): Boolean {
        return this.item.canStackWith(itemPile.item)
    }

    fun destroy() {
        amount = 0
    }

    override fun toString(): String {
        return "[ItemPile t:$item a:$amount i:$inventory x:$x y:$y ]"
    }
}
