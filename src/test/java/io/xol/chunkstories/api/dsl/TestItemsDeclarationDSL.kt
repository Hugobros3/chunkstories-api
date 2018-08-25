package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.item.Item
import io.xol.chunkstories.api.item.ItemDefinition
import io.xol.chunkstories.api.item.ItemVoxel
import org.junit.Test

class TestItemsDeclarationDSL {

    var ctx: ItemDeclarationsContext? = null

    @Test
    fun testItemsDeclarationsDSL() {
        ctx?.apply {
            item {
                name = "apple"
                maxStackSize = 5

                representation {

                }
            }

            item(ItemCustomClass::class) {
                name = "merde"

                prototype {
                    customModel = "Wow !"
                    maxRounds = 10
                }

                representation {
                    modelInstance("./models/custom/${item.customModel ?: "cube"}.obj")
                }
            }
        }
    }

    class ItemCustomClass(definition: ItemDefinition) : Item(definition) {
        var customModel: String? = null
        var maxRounds = 50
    }
}