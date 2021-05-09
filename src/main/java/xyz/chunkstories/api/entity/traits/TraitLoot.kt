//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.makeEntityLootTableFromJson

class TraitLoot(entity: Entity) : Trait(entity) {
    override val traitName = "loot"

    val lootTable = makeEntityLootTableFromJson(entity.definition.properties["drops"] ?: Json.Value.Null, entity.definition.store.parent)
}