//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asArray
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.loot.LootTable
import xyz.chunkstories.api.loot.makeLootTableFromJson

class EntityLootTable(val default: LootTable, val predicates: List<MatchablePredicate>) {
    abstract class MatchablePredicate(val drops: LootTable) {
        abstract fun match(lastDamageCause: DamageCause?) : Boolean
    }

    class MatchWeaponName(val weaponName: String, drops: LootTable) : MatchablePredicate(drops) {
        override fun match(lastDamageCause: DamageCause?) = (lastDamageCause as? DamageCause.Entity)?.weapon?.name == weaponName
    }
    class MatchKiller(val entityName: String, drops: LootTable) : MatchablePredicate(drops) {
        override fun match(lastDamageCause: DamageCause?) = (lastDamageCause as? DamageCause.Entity)?.entity?.definition?.name == entityName
    }

    fun spawn(lastDamageCause: DamageCause?): List<Pair<Item, Int>> {
        for(predicate in predicates) {
            if(predicate.match(lastDamageCause))
                return predicate.drops.spawn()
        }

        return default.spawn()
    }
}

fun makeEntityLootTableFromJson(json: Json, content: Content): EntityLootTable {
    if(json is Json.Dict && json["type"].asString == "match_tool") {
        val defaultTable = makeLootTableFromJson(json["default"] ?: Json.Value.Bool(true), content, null)
        val predicates = json["predicates"].asArray?.elements?.map {
            if(it !is Json.Dict)
                throw Exception("Predicates of a block loot table must be dicts ! ($it)")
            val drops = makeLootTableFromJson(it["drops"]  ?: Json.Value.Bool(true), content, null)

            val matchType = it["match"].asString ?: throw Exception("Missing match type. ($it)")
            when(matchType) {
                "weapon" -> {
                    val matchedWeaponName = it["name"].asString ?: throw Exception("Weapon name can't be null. ($it)")
                    EntityLootTable.MatchWeaponName(matchedWeaponName, drops)
                }
                "entity" -> {
                    val matchedEntityName = it["entity"].asString ?: throw Exception("Entity name can't be null. ($it)")
                    EntityLootTable.MatchKiller(matchedEntityName, drops)
                }
                else -> throw Exception("Unsupported match type '$matchType'. ($it)")
            }
        } ?: emptyList()

        return EntityLootTable(defaultTable, predicates)
    } else {
        return EntityLootTable(makeLootTableFromJson(json, content, null), emptyList())
    }
}