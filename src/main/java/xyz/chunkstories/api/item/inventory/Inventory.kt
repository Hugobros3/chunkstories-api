//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import org.slf4j.LoggerFactory
import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Integer.min
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

// . . . . . . . . . .
// . . . . . . . . . .
// . . . . . . . . . .
// . . . . . . . . . .

// . . . . . . B B B .
// . . . . . . B B B .
// . . . A A . . . . .
// . X . A A . . . . .

/** Mostly the data structure that actually holds items  */
class Inventory(val width: Int, val height: Int, val owner: InventoryOwner? = null, private val callbacks: InventoryCallbacks?) {
    internal val lock = ReentrantReadWriteLock()
    internal val grid: Array<Array<ItemPile?>> = Array(width) { arrayOfNulls<ItemPile>(height) }

    constructor(stream: DataInputStream, translator: ContentTranslator, owner: InventoryOwner, callbacks: InventoryCallbacks?) : this(stream.readInt(), stream.readInt(), owner, callbacks) {
        loadContentsFromStream(stream, translator)
    }

    /** Returns the ItemPile in that strict position */
    fun getItemPileAt(x: Int, y: Int): ItemPile? {
        if (!inBounds(x, y))
            throw Exception("Out of bounds exception: ($x,$y) isn't in range [0..$width[,[0..$height[")
        lock.read {
            return grid[x][y]
        }
    }

    /** Returns the ItemPile in that position. This functions considers the fact
     * that some items are wider than others, thus checking different positions can
     * return the same items.  */
    fun getAnyItemPileAt(x: Int, y: Int): ItemPile? {
        if (!inBounds(x, y))
            throw Exception("Out of bounds exception: ($x,$y) isn't in range [0..$width[,[0..$height[")

        lock.read {
            for (i in 0 until width)
                for (j in 0 until height) {
                    val pile = grid[i][j] ?: continue

                    if (x >= pile.x && x < pile.x + pile.item.definition.slotsWidth)
                        if (y >= pile.y && y < pile.y + pile.item.definition.slotsHeight)
                            return pile
                }


            return null
        }
    }

    internal fun movePileToInventory(x: Int, y: Int, source: ItemPile?, item: Item, amount: Int, dryRun: Boolean): Int {
        if (!inBounds(x, y))
            throw Exception("Out of bounds exception: ($x,$y) isn't in range [0..$width[,[0..$height[")

        if(amount < 1)
            throw Exception("Illegal amount")

        if(callbacks?.isItemAccepted(item) == false)
            return amount

        fun withSourceLocked(): Int {
            if (source != null && source.amount < amount)
                throw Exception("Source item pile amount < amount to move")

            // Lock this inventory to avoid any concurrent item moves
            lock.write {
                if (!wouldFit(x, y, item))
                    return amount

                val overlap = wouldOverlapWith(x, y, item)

                // The space is clear !
                if (overlap.isEmpty()) {
                    if (!dryRun) {
                        source?.let {
                            source.amount -= amount
                        }

                        grid[x][y] = ItemPile(this, x, y, item.duplicate(), amount)
                    }
                    return 0
                } else {
                    var amountLeft = amount
                    for (candidate in overlap.filter { it.item.canStackWith(item) }) {
                        if (candidate === source) {
                            println("moving to self !")
                            continue
                        }

                        val freeSpace = item.definition.maxStackSize - candidate.amount

                        if (freeSpace > amountLeft) {
                            if (!dryRun) {
                                source?.let { it.amount -= amountLeft }
                                candidate.amount += amountLeft
                            }
                            amountLeft = 0
                        } else if (freeSpace > 0) {
                            if (!dryRun) {
                                source?.let { it.amount -= freeSpace }
                                candidate.amount += freeSpace
                            }
                            amountLeft -= min(freeSpace, amountLeft)
                        }


                        if (amountLeft == 0)
                            break
                    }
                    return amountLeft
                }
            }
        }

        return source?.inventory?.lock?.write { withSourceLocked() } ?: withSourceLocked()
    }

    /** Checks if a spot in the inventory is eligible for placement of an ItemPile.
     * Takes into account the size of the items, as well as item stacking.  */

    @JvmOverloads
    fun ItemPile.canMoveTo(x: Int, y: Int, itemPile: ItemPile, amount: Int = itemPile.amount): Int = movePileToInventory(x, y, this, item, amount, true)

    @JvmOverloads
    fun ItemPile.moveTo(x: Int, y: Int, itemPile: ItemPile, amount: Int = itemPile.amount): Int = movePileToInventory(x, y, this, item, amount, false)

    @JvmOverloads
    fun canPlaceItemAt(x: Int, y: Int, item: Item, amount: Int = 1) = movePileToInventory(x, y, null, item, amount, true)

    @JvmOverloads
    fun placeItemAt(x: Int, y: Int, item: Item, amount: Int = 1) = movePileToInventory(x, y, null, item, amount, false)

    //fun placeItemPileAt(x: Int, y: Int, itemPile: ItemPile): Int = movePileToInventory(x, y, itemPile, false)

    /** Ensures some coordinates are in bounds within the inventory */
    private fun inBounds(x: Int, y: Int) = x in 0 until width && y in 0 until height

    /** Ensures placing a pile at specific coordinates wouldn't go over the inventory bounds because of the size of the item */
    private fun wouldFit(x: Int, y: Int, item: Item): Boolean =
            x + item.definition.slotsWidth <= width && y + item.definition.slotsHeight <= height

    /** Returns a set contaning all the piles that would collide with `itemPile` were it to be placed at (x,y) */
    private fun wouldOverlapWith(x: Int, y: Int, item: Item): Set<ItemPile> {
        val set = mutableSetOf<ItemPile>()

        for (i in x until x + item.definition.slotsWidth)
            for (j in y until y + item.definition.slotsHeight) {
                val any = getAnyItemPileAt(i, j) ?: continue
                set.add(any) // adding multiple copies of the same item is an idempotent operation
            }

        return set
    }

    @JvmOverloads
    fun addItem(item: Item, amount: Int = 1): Int {
        if(amount < 1)
            throw Exception("Illegal amount")

        lock.write {
            var amountLeft = amount
            for (x in 0 until width)
                for (y in 0 until height) {
                    val couldntPlace = canPlaceItemAt(x, y, item, amountLeft)
                    val amount2place = amountLeft - couldntPlace

                    if(amount2place == 0)
                        continue

                    placeItemAt(x, y, item, amount2place)

                    amountLeft -= amount2place

                    if (amountLeft == 0) {
                        return 0
                    }
                }

            return amountLeft
        }
    }

    @JvmOverloads
    fun setItemAt(x: Int, y: Int, item: Item?, amount: Int = 1, force: Boolean = false): Boolean {
        if (!inBounds(x, y))
            throw Exception("Out of bounds exception: ($x,$y) isn't in range [0..$width[,[0..$height[")

        if(amount < 1)
            throw Exception("Illegal amount")

        lock.write {
            if(item == null || amount < 1) {
                grid[x][y] = null
                refreshItemSlot(x, y, null)
                return true
            } else {
                val overlap = wouldOverlapWith(x, y, item)

                return when {
                    overlap.isEmpty() -> {
                        placeItemAt(x, y, item, amount)
                        true
                    }
                    force -> {
                        for(pile in overlap) {
                            pile.amount = 0
                        }
                        placeItemAt(x, y, item, amount)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    val contents: Collection<ItemPile>
        get() {
            val set = mutableListOf<ItemPile>()
            for (x in 0 until width)
                for (y in 0 until height) {
                    val pile = grid[x][y] ?: continue
                    set.add(pile)
                }
            return set
        }

    /** Called when the entire inventory contents have been modified */
    fun refreshCompleteInventory() {
        callbacks?.refreshCompleteInventory()
    }

    /** Called when a single item slot has been modified */
    fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?) {
        callbacks?.refreshItemSlot(x, y, pileChanged)
    }

    /** Saves the inventory content to a data stream */
    fun saveToStream(stream: DataOutputStream, translator: ContentTranslator) {
        stream.writeInt(width)
        stream.writeInt(height)

        saveContentsToStream(stream, translator)
    }

    fun loadFromStream(stream: DataInputStream, translator: ContentTranslator) {
        val width = stream.readInt()
        val height= stream.readInt()

        if(width != this.width || height != this.height)
            throw Exception("Oops! Stored inventory size is :($width, $height) but inventory loading it is (${this.width},${this.height})")

        loadContentsFromStream(stream, translator)
    }

    fun saveContentsToStream(stream: DataOutputStream, translator: ContentTranslator) {
        for (i in 0 until width)
            for (j in 0 until height) {
                val pile = grid[i][j]
                if (pile == null)
                    stream.writeInt(0)
                else {
                    pile.saveIntoStream(translator, stream)
                }
            }
    }

    fun loadContentsFromStream(stream: DataInputStream, translator: ContentTranslator) {
        for (i in 0 until width)
            for (j in 0 until height) {
                try {
                    val (item, amount) = obtainItemPileFromStream(translator, stream)
                    grid[i][j] = ItemPile(this, i, j, item, amount)
                } catch (e: NullItemException) {
                    // Don't do anything about it, no big deal
                } catch (e: UndefinedItemTypeException) {
                    // This is slightly more problematic
                    logger.error("Undefined item: ", e)
                }

            }
    }

    fun size(): Int {
        var size = 0
        for (itemPile in contents) {
            size++
        }
        return size
    }

    /** Removes all the item piles from this inventory */
    fun clear() {
        grid.forEach { it.fill(null) }
        refreshCompleteInventory()
    }

    val inventoryName: String
        get() = callbacks?.inventoryName ?: "Unnamed Inventory"

    fun isAccessibleTo(entity: Entity): Boolean {
        return callbacks?.isAccessibleTo(entity) ?: true
    }

    companion object {
        val logger = LoggerFactory.getLogger("inventory")
    }
}

@JvmOverloads
fun ItemPile.canMoveTo(destInventory: Inventory, x: Int, y: Int, amount: Int = this.amount): Int = destInventory.movePileToInventory(x, y, this, item, amount, true)


@JvmOverloads
fun ItemPile.moveTo(destInventory: Inventory, x: Int, y: Int, amount: Int = this.amount): Int = destInventory.movePileToInventory(x, y, this, item, amount, false)