package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.item.inventory.ItemPile
import io.xol.chunkstories.api.util.kotlin.random
import java.lang.Math.random
import kotlin.math.min

class LootRules(val instructions: LootTableBuildingContext.() -> Unit) {
    fun spawn() = LootTableBuilder(ANDLootTable(), instructions).table.spawn()
}

interface LootTableBuildingContext {
    fun entry(f: LootTableEntry.() -> Unit)

    fun and(f: LootTableBuildingContext.() -> Unit)

    fun or(f: LootTableBuildingContext.() -> Unit)

    var probability : Double
}

class LootTableBuilder(val table: CompoundLootTableEntry, instructions: LootTableBuildingContext.() -> Unit) : LootTableBuildingContext {
    init {
        this.apply(instructions)
    }

    override fun entry(f: LootTableEntry.() -> Unit) { table.entries.add(LootTableEntry().apply(f)) }

    override fun and(f: LootTableBuildingContext.() -> Unit) {
        val subctx = LootTableBuilder(ANDLootTable(), f)
        table.entries.add(subctx.table)
    }

    override fun or(f: LootTableBuildingContext.() -> Unit) {
        val subctx = LootTableBuilder(ORLootTable(), f)
        table.entries.add(subctx.table)
    }

    override var probability : Double
        set(value) { table.probability = value }
        get() = 0.0
}

class ORLootTable : CompoundLootTableEntry() {
    override fun spawn(): List<ItemPile> {
        val pTotal = entries.sumByDouble { it.probability }
        val r = min(1.0, random() * pTotal)

        var acc = 0.0
        for (entry in entries) {
            if (r in acc..(acc + entry.probability))
                return entry.spawn()

            acc += entry.probability
        }

        return noItems
    }
}

class ANDLootTable : CompoundLootTableEntry() {

    override fun spawn(): List<ItemPile> {
        if (random() < probability)
            return noItems

        val items = mutableListOf<ItemPile>()
        for (entry in entries) {
            items += entry.spawn()
        }
        return items
    }
}

abstract class CompoundLootTableEntry : LootTable() {
    val entries = mutableListOf<LootTable>()
}

class LootTableEntry : LootTable() {
    lateinit var items: List<ItemPile>
    var amount: IntRange = 1..1

    override fun spawn(): List<ItemPile> {
        if (random() < probability)
            return noItems

        val multiplier = amount.random()

        val spawned = items.map { it.duplicate() }
        for (pile in spawned)
            pile.amount *= multiplier

        return spawned
    }
}

val noItems = emptyList<ItemPile>()

abstract class LootTable {
    var probability: Double = 1.0

    abstract fun spawn(): List<ItemPile>
}