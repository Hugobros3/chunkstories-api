//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.item.Item
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.write

/** A tangible pile of items in an inventory  */
class ItemPile(val inventory: Inventory, val x: Int, val y: Int, val item: Item, amount: Int = 1) {
    var amount = amount
        set(value) {
            inventory.lock.write {
                field = value

                // ItemPiles are smart enough to self-delete when they become == 0 !
                if (value == 0)
                    inventory.grid[x][y] = null
                else
                    inventory.refreshItemSlot(x, y, this)
            }
        }

    //val lock = ReentrantLock()
    /*var inventory: Inventory? = null
    var x: Int = 0
    var y: Int = 0*/

    /** Try to move an item to another slot
     *
     * @param destinationInventory new slot's inventory
     * @param destinationX
     * @param destinationY
     * @return null if successfull, this if not.
     */
    // @SuppressWarnings("unchecked")
    /*fun moveItemPileTo(destinationInventory: Inventory?, destinationX: Int, destinationY: Int, amountToTransfer: Int): Boolean {
        val currentInventory = this.inventory

        // If moving to itself
        if (destinationInventory != null && currentInventory != null && destinationInventory == currentInventory) {
            var alreadyHere: ItemPile? = null
            var i = 0
            val w = this.item.definition.slotsWidth
            val h = this.item.definition.slotsHeight
            // Tryhard to find out if it touches itself
            do {
                if (alreadyHere != null && alreadyHere == this) {
                    // Remove temporarily
                    destinationInventory.setItemPileAt(x, y, null)

                    // Check if can be placed now
                    if (destinationInventory.canPlaceItemAt(destinationX, destinationY, this)) {
                        destinationInventory.setItemPileAt(destinationX, destinationY, this)
                        return true
                    }

                    // Add back if it couldn't
                    destinationInventory.setItemPileAt(x, y, this)
                    return false
                }

                alreadyHere = destinationInventory.getItemPileAt(destinationX + i % w, destinationY + i / w)
                i++
            } while (i < w * h)
        }

        // We duplicate the pile and limit it's amount
        val pileToSend = this.duplicate()
        pileToSend.amount = (amountToTransfer)

        // The amount we're not trying to transfer
        val leftAmountBeforeTransaction = this.amount - amountToTransfer

        var leftFromTransaction: ItemPile? = null
        // Moving an item to a null inventory would destroy it so leftFromTransaction
        // stays nulls in that case
        if (destinationInventory != null)
            leftFromTransaction = destinationInventory.placeItemPileAt(destinationX, destinationY, pileToSend)

        // If something was left from the transaction ( incomplete )
        if (leftFromTransaction != null)
            this.amount = leftAmountBeforeTransaction + leftFromTransaction.amount
        else if (leftAmountBeforeTransaction > 0)
            this.amount = leftAmountBeforeTransaction
        else currentInventory?.setItemPileAt(this.x, this.y, null)// If everything was moved we destroy this pile ... if it ever existed (
        // /dev/null inventories, creative mode etc )
        // If nothing was left but we only moved part of the stuff

        // Success conditions : either we transfered all or we transfered at least one
        return leftFromTransaction == null || leftFromTransaction.amount < amountToTransfer
    }*/

    fun canStackWith(itemPile: ItemPile): Boolean {
        return this.item.canStackWith(itemPile.item)
    }

    /** Returns an exact copy of this pile  */
    /*fun duplicate(): ItemPile {
        lock.withLock {
            val pile = ItemPile(this.item, this.amount)
            val data = ByteArrayOutputStream()
            try {
                item.save(DataOutputStream(data))

                val stream = ByteArrayInputStream(data.toByteArray())
                val dis = DataInputStream(stream)

                item.load(dis)
                dis.close()
            } catch (e: IOException) {
            }

            return pile
        }
    }*/

    fun destroy() {
        amount = 0
    }

    override fun toString(): String {
        return "[ItemPile t:$item a:$amount i:$inventory x:$x y:$y ]"
    }
}
