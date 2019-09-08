package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.item.Item

fun Inventory.serialize(contentTranslator: ContentTranslator, useItemIds: Boolean = false): Json.Dict = Json.Dict(
        mapOf(
                "width" to Json.Value.Number(width.toDouble()),
                "height" to Json.Value.Number(height.toDouble()),
                "contents" to Json.Array(
                        contents.map {
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

fun Inventory.deserialize(contentTranslator: ContentTranslator, json: Json.Dict) {
    val width = json["width"].asInt!!
    val height = json["height"].asInt!!

    if(this.width != width || this.height != height)
        throw Exception("Mismatched inventory sizes")

    this.clear()

    val contents = json["contents"].asArray!!
    for(entry in contents.elements) {
        val entry = entry.asDict!!
        val x = entry["x"].asInt!!
        val y = entry["y"].asInt!!
        val amount = entry["amount"].asInt!!

        val item = entry["itemId"].asInt?.let { contentTranslator.getItemForId(it)!!.newItem<Item>() } ?:
        entry["itemName"].asString?.let { contentTranslator.content.items.getItemDefinition(it)?.newItem<Item>() ?: throw Exception("Missing item: $it") }!!

        item.deserialize(entry["itemData"].asDict!!)

        this.setItemAt(x, y, item, amount)
    }
}