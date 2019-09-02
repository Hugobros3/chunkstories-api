package xyz.chunkstories.api.crafting

import org.slf4j.LoggerFactory
import xyz.chunkstories.api.gui.inventory.InventorySlot
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile

abstract class Recipe(val result: Pair<ItemDefinition, Int>) {
    companion object {
        val logger = LoggerFactory.getLogger("crafting")
    }

    abstract fun canCraftUsing(craftingAreaSlots: Array<Array<InventorySlot.FakeSlot>>): Boolean
    abstract fun canCraftUsing(suppliedIngredients: Map<ItemPile, Int>): Boolean

    abstract fun craftUsing(craftingAreaSlots: Array<Array<InventorySlot.FakeSlot>>, destinationInventory: Inventory, destX: Int, destY: Int)
    abstract fun craftUsing(suppliedIngredients: Map<ItemPile, Int>, destinationInventory: Inventory, destX: Int, destY: Int)
}

class AmorphRecipe(val ingredients: List<Ingredient>) {
    data class Ingredient(val item: ItemDefinition, val quantity: Int)
}

class PatternedRecipe(val pattern: Array<Array<ItemDefinition?>>, result: Pair<ItemDefinition, Int>) : Recipe(result) {
    val width = pattern.size
    val height = pattern[0].size

    val requirements: Map<ItemDefinition, Int>

    init {
        // this is what makes FP great :)
        requirements = pattern.flatten().filterNotNull().groupBy { it }.mapValues { it.value.size }
    }

    override fun canCraftUsing(craftingAreaSlots: Array<Array<InventorySlot.FakeSlot>>): Boolean {
        val slotsWidth = craftingAreaSlots.size
        val slotsHeight = craftingAreaSlots[0].size

        if (slotsWidth < width || slotsHeight < height) {
            //Recipe.logger.warn("Cannot craft ${result.name} because grid is too small")
            return false
        }

        for (offsetX in 0..slotsWidth - width) {
            outer@ for (offsetY in 0..slotsHeight - height) {
                val list = mutableListOf<InventorySlot.FakeSlot>()

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val slot = craftingAreaSlots[x + offsetX][y + offsetY] as? InventorySlot.FakeSlot ?: throw Exception("these have to be fake")

                        val displayContents = slot.visibleContents

                        val shouldBe = pattern[x][y]
                        //println("$x $y should be ${shouldBe?.name} is ${displayContents?.first?.name}")

                        if (shouldBe == null && displayContents == null)
                            continue
                        else {
                            if (displayContents?.first?.definition == shouldBe) {
                                //list.add(slot)
                                continue
                            }
                        }

                        continue@outer
                    }
                }

                return true
            }
        }

        //Recipe.logger.debug("Cannot craft ${result.name} because no arrangements matched the recipe")
        return false
    }

    override fun canCraftUsing(suppliedIngredients: Map<ItemPile, Int>) : Boolean {
        val providedCounts = suppliedIngredients.entries.map { Pair(it.key.item.definition, it.value) }.groupBy { it.first }.mapValues { it.value.sumBy { it.second } }
        for((item, count) in requirements) {
            if(providedCounts[item] ?: 0 < count)
                return false
        }
        return true
    }

    override fun craftUsing(suppliedIngredients: Map<ItemPile, Int>, destinationInventory: Inventory, destX: Int, destY: Int) {
        if(!canCraftUsing(suppliedIngredients))
            return

        for((itemPile, amount) in suppliedIngredients) {
            itemPile.amount -= amount
        }
        destinationInventory.placeItemAt(destX, destY, result.first.newItem(), result.second)
    }

    override fun craftUsing(craftingAreaSlots: Array<Array<InventorySlot.FakeSlot>>, destinationInventory: Inventory, destX: Int, destY: Int) {
        if(!canCraftUsing(craftingAreaSlots))
            return

        val pileConsume = mutableMapOf<ItemPile, Int>()

        for (slot in craftingAreaSlots.flatten().filter { it.stealingFrom.size > 0 }) {
            val slotWhereTheRealItemIs = slot.stealingFrom.removeAt(0)
            slotWhereTheRealItemIs.stolen--

            val srcPile = slotWhereTheRealItemIs.actualItemPile ?: return
            pileConsume[srcPile] = pileConsume.getOrDefault(srcPile, 0) + 1
        }

        craftUsing(pileConsume, destinationInventory, destX, destY)
    }
}