//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.loot

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
}
