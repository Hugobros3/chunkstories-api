package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.makeEntityLootTableFromJson

class TraitLoot(entity: Entity) : Trait(entity) {
    val lootTable = makeEntityLootTableFromJson(entity.definition["drops"] ?: Json.Value.Null, entity.definition.store.parent)
}