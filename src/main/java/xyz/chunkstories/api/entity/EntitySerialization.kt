//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.entity.traits.serializable.TraitSerializable
import xyz.chunkstories.api.world.World

object EntitySerialization {
    fun serializeEntity(entity: Entity) : Json {
        return Json.Dict(mapOf(
                "uuid" to Json.Value.Text(entity.UUID.toString()),
                "entityType" to Json.Value.Text(entity.definition.name),
                "traits" to Json.Dict(entity.traits.all().filterIsInstance<TraitSerializable>().map { Pair(it.serializedTraitName, it.serialize()) }.toMap())
        ))
    }

    fun deserializeEntity(world: World, json: Json) : Entity {
        if(json !is Json.Dict)
            throw Exception("Can't deserialize anything but a Dict into an Entity")
        val uuid = json["uuid"].asString!!.toLong()
        val entityType = world.content.entities.getEntityDefinition(json["entityType"].asString!!)!!
        val entity = entityType.newEntity<Entity>(world)
        entity.UUID = uuid

        for((name, traitJsonPayload) in json["traits"].asDict!!.elements) {
            val trait = entity.traits.all().find { it is TraitSerializable && it.serializedTraitName == name } as? TraitSerializable ?: continue
            trait.deserialize(traitJsonPayload)
        }

        return entity
    }
}