//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.item.Item

object InventorySerialization {
    /**
     * Serialize an inventory to Json
     */
    fun serializeInventory(inventory: Inventory, contentTranslator: ContentTranslator, useItemIds: Boolean = false): Json.Dict = Json.Dict(
            mapOf(
                    "width" to Json.Value.Number(inventory.width.toDouble()),
                    "height" to Json.Value.Number(inventory.height.toDouble()),
                    "contents" to Json.Array(
                            inventory.contents.map {
                                Json.Dict(
                                        mapOf(
                                                "x" to Json.Value.Number(it.x.toDouble()),
                                                "y" to Json.Value.Number(it.y.toDouble()),
                                                if (useItemIds)
                                                    "itemName" to Json.Value.Text(it.item.definition.name)
                                                else
                                                    "itemId" to Json.Value.Number(contentTranslator.getIdForItem(it.item).toDouble()),
                                                "amount" to Json.Value.Number(it.amount.toDouble()),
                                                "itemData" to it.item.serialize()
                                        )
                                )
                            }
                    )
            )
    )

    /**
     * Clears and reload an inventory based on the json. Will fail if the json describes an inventory with a different size.
     */
    fun deserializeInventory(inventory: Inventory, contentTranslator: ContentTranslator, json: Json.Dict) {
        val width = json["width"].asInt!!
        val height = json["height"].asInt!!

        if (inventory.width != width || inventory.height != height)
            throw Exception("Mismatched inventory sizes")

        inventory.clear()

        val contents = json["contents"].asArray!!
        for (entry in contents.elements) {
            val entry = entry.asDict!!
            val x = entry["x"].asInt!!
            val y = entry["y"].asInt!!
            val amount = entry["amount"].asInt!!

            val item = entry["itemId"].asInt?.let { contentTranslator.getItemForId(it)!!.newItem<Item>() }
                    ?: entry["itemName"].asString?.let {
                        contentTranslator.content.items.getItemDefinition(it)?.newItem<Item>()
                                ?: throw Exception("Missing item: $it")
                    }!!

            item.deserialize(entry["itemData"].asDict!!)

            inventory.setItemAt(x, y, item, amount)
        }
    }

    fun serializeItemAndAmount(item: Item?, amount: Int): Json {
        if (item != null)
            return Json.Dict(
                    mapOf(
                            "itemName" to Json.Value.Text(item.definition.name),
                            "amount" to Json.Value.Number(amount.toDouble()),
                            "itemData" to item.serialize()
                    )
            )
        return Json.Value.Null
    }

    fun deserializeItemAndAmount(contentTranslator: ContentTranslator, json: Json): Pair<Item?, Int> {
        if (json is Json.Dict) {
            val amount = json["amount"].asInt!!
            val item = json["itemId"].asInt?.let { contentTranslator.getItemForId(it)!!.newItem<Item>() }
                    ?: json["itemName"].asString?.let {
                        contentTranslator.content.items.getItemDefinition(it)?.newItem<Item>()
                                ?: throw Exception("Missing item: $it")
                    }!!

            item.deserialize(json["itemData"].asDict!!)
            return Pair(item, amount)
        } else {
            return Pair(null, 0)
        }
    }
}