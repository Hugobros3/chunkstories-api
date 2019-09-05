package xyz.chunkstories.api.voxel

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asArray
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.loot.LootTable
import xyz.chunkstories.api.loot.makeLootTableFromJson

class BlockLootTable(val default: LootTable, val predicates: List<MatchablePredicate>) {
    abstract class MatchablePredicate(val drops: LootTable) {
        abstract fun match(miningTool: MiningTool) : Boolean
    }

    class MatchItem(val itemName: String, drops: LootTable) : MatchablePredicate(drops) {
        override fun match(miningTool: MiningTool) = (miningTool as? Item)?.name == itemName
    }
    class MatchToolType(val toolTypeName: String, drops: LootTable) : MatchablePredicate(drops) {
        override fun match(miningTool: MiningTool) = miningTool.toolTypeName == toolTypeName
    }

    fun spawn(miningTool: MiningTool): List<Pair<Item, Int>> {
        for(predicate in predicates) {
            if(predicate.match(miningTool))
                return predicate.drops.spawn()
        }

        return default.spawn()
    }
}

fun makeBlockLootTableFromJson(json: Json, content: Content, default: Pair<ItemDefinition, Int>): BlockLootTable {
    if(json is Json.Dict && json["type"].asString == "match_tool") {
        val defaultTable = makeLootTableFromJson(json["default"] ?: Json.Value.Bool(true), content, default)
        val predicates = json["predicates"].asArray?.elements?.map {
            if(it !is Json.Dict)
                throw Exception("Predicates of a block loot table must be dicts ! ($it)")
            val drops = makeLootTableFromJson(it["drops"]  ?: Json.Value.Bool(true), content, default)

            val matchType = it["match"].asString ?: throw Exception("Missing match type. ($it)")
            when(matchType) {
                "tool_type" -> {
                    val matchedToolType = it["tool_type"].asString ?: throw Exception("Tool type can't be null. ($it)")
                    BlockLootTable.MatchToolType(matchedToolType, drops)
                }
                "item" -> {
                    val matchedItemName = it["item"].asString ?: throw Exception("Item name can't be null. ($it)")
                    BlockLootTable.MatchItem(matchedItemName, drops)
                }
                else -> throw Exception("Unsupported match type '$matchType'. ($it)")
            }
        } ?: emptyList()

        return BlockLootTable(defaultTable, predicates)
    } else {
        return BlockLootTable(makeLootTableFromJson(json, content, default), emptyList())
    }
}