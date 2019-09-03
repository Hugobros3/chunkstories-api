//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.loot

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.util.kotlin.random
import java.lang.Math.random

private val noItems = emptyList<Pair<Item, Int>>()

sealed class LootTable(val weight: Double = 1.0) {

    abstract fun spawn(): List<Pair<Item, Int>>

    class PickOne(val entries: List<LootTable>, weight: Double) : LootTable(weight) {
        val totalWeights = entries.sumByDouble { it.weight }

        override fun spawn(): List<Pair<Item, Int>> {
            val r = random() * totalWeights

            var acc = 0.0
            for (entry in entries) {
                if (r in acc..(acc + entry.weight))
                    return entry.spawn()

                acc += entry.weight
            }

            System.err.println("Math issue here")
            return noItems
        }
    }

    class AllOf(val entries: List<LootTable>, weight: Double) : LootTable(weight) {
        override fun spawn(): List<Pair<Item, Int>> {
            val items = mutableListOf<Pair<Item, Int>>()
            for (entry in entries) {
                items += entry.spawn()
            }
            return items
        }
    }

    class Entry(val spawns: ItemDefinition, val amountRange: IntRange = 1..1, weight: Double) : LootTable(weight) {
        override fun spawn(): List<Pair<Item, Int>> {
            val amount = amountRange.random()
            return listOf(Pair(spawns.newItem(), amount))
        }
    }

    class Nothing(weight: Double) : LootTable(weight) {
        override fun spawn(): List<Pair<Item, Int>> {
            return noItems
        }
    }

    internal class Lazy(json: Json, content: Content, default: Pair<ItemDefinition, Int>? = null) : LootTable(1.0) {
        private val realTable: LootTable by lazy { parseLootTableFromJson(json, content, default) }

        override fun spawn(): List<Pair<Item, Int>> = realTable.spawn()
    }
}

/** To handle the fact a loot table can be loaded before the content it references, the public API for loot tables lazily resolves them when first used. */
fun makeLootTableFromJson(json: Json, content: Content, default: Pair<ItemDefinition, Int>? = null): LootTable = LootTable.Lazy(json, content, default)

private fun parseLootTableFromJson(json: Json, content: Content, default: Pair<ItemDefinition, Int>? = null): LootTable = when(json) {
    Json.Value.Null -> LootTable.Nothing(1.0)
    is Json.Value.Text -> {
        val itemDef = content.items.getItemDefinition(json.text) ?: throw Exception("Can't load loot table: item ${json.text} not found in loaded content. ($json)")
        LootTable.Entry(itemDef, 1..1, 1.0)
    }
    is Json.Value.Number -> throw Exception("Can't interpret $json as a loot table entry.")
    is Json.Value.Bool -> {
        if(json.value && default != null)
            LootTable.Entry(default.first, default.second..default.second, 1.0)
        else if(!json.value)
            LootTable.Nothing(1.0)
        else
            throw Exception("Ambiguous loot table: 'true' means nothing outside of a context that has a canonical default ! ($json)")
    }
    is Json.Array -> {
        if(json.elements.isEmpty())
            LootTable.Nothing(1.0)
        else {
            val itemName = json.elements[0].asString ?: throw Exception("Expected an item name but ${json.elements[0]} can't be interpreted as one. ($json)")
            val itemDef = content.items.getItemDefinition(itemName) ?: throw Exception("Can't load loot table: item $itemName not found in loaded content. ($json)")

            val count = json.elements.getOrNull(1).asInt ?: 1
            LootTable.Entry(itemDef, count..count, 1.0)
        }
    }
    is Json.Dict -> {
        val weight = json["weight"].asDouble ?: 1.0
        when(val type = json["type"].asString ?: "unknown") {
            "unknown" -> {
                if(json["item"].asString != null) {
                    val itemName = json["item"].asString ?: throw Exception("Expected an item name but ${json["item"]} can't be interpreted as one. ($json)")
                    val itemDef = content.items.getItemDefinition(itemName)
                            ?: throw Exception("Can't load loot table: item $itemName not found in loaded content. ($json)")

                    val amountMin = json["amount_min"].asInt ?: json["amount"].asInt ?: 1
                    val amountMax = json["amount_max"].asInt ?: json["amount"].asInt ?: 1
                    if (amountMax < amountMin)
                        throw Exception("amountMax can't be lower than amountMin ( $json )")

                    LootTable.Entry(itemDef, amountMin..amountMax, weight)
                } else if(json["loot_table"].asString != null) {
                    val referencedLootTableName = json["loot_table"].asString ?: throw Exception("Expected a loot table name but ${json["loot_table"]} can't be interpreted as one. ($json)")
                    val referencedLootTable = content.lootTables[referencedLootTableName] ?: throw Exception("Loot table '$referencedLootTableName' can't be found in loaded content.")
                    referencedLootTable
                } else {
                    throw Exception("Unrecognized loot table typeZ. ($json)")
                }
            }
            "nothing" -> {
                LootTable.Nothing(weight)
            }
            "pick_one" -> {
                val entries = json["entries"].asArray ?: throw Exception("For 'pick_one' loot tables, 'entries' must be a valid Json Array. ($json)")
                LootTable.PickOne(entries.elements.map { parseLootTableFromJson(it, content, default) }, weight)
            }
            "all_of" -> {
                val entries = json["entries"].asArray ?: throw Exception("For 'all_of' loot tables, 'entries' must be a valid Json Array. ($json)")
                LootTable.AllOf(entries.elements.map { parseLootTableFromJson(it, content, default) }, weight)
            }
            else -> throw Exception("Unrecognized loot table type '$type'. ($json)")
        }
    }
}