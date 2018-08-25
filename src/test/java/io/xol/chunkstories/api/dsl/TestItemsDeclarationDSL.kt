package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.item.Item
import io.xol.chunkstories.api.item.ItemDeclaration
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
                    model("./models/custom/${item.customModel ?: "cube"}.obj")
                }
            }
        }
    }

    class ItemCustomClass(definition: ItemDeclaration<*>) : Item(definition) {
        var customModel: String? = null
        var maxRounds = 50
    }
}