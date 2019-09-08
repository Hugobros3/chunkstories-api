//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

@file:Suppress("UNCHECKED_CAST")

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.util.kotlin.initOnce
import xyz.chunkstories.api.world.World
import java.lang.reflect.Constructor

class EntityDefinition(val store: Content.EntityDefinitions, name: String, properties: Json.Dict) : Definition(name, properties) {
    /** When added to the game content, either by being loaded explicitly or programatically, will be set to an integer
     * value. Attempting to manually override/set this identifier yourself will result in a house fire. */
    var assignedId : Int by initOnce()

    val clazz: Class<Entity>
    private val constructor: Constructor<Entity>

    init {
        clazz = this["class"].asString?.let {
            store.parent.modsManager.getClassByName(it)?.let { clazz ->
                if(Entity::class.java.isAssignableFrom(clazz))
                    clazz as Class<Entity>
                else
                    throw Exception("The custom class has to extend the Voxel class !")
            }
        }  ?: Entity::class.java

        constructor = try {
            clazz.getConstructor(EntityDefinition::class.java, World::class.java)
        } catch (e: NoSuchMethodException) {
            throw Exception("Your custom class, $clazz, lacks the correct Entity(EntityDefinition, World) constructor.")
        }
    }

    fun <E : Entity> newEntity(world: World) : E {
        val entity = (constructor.newInstance(this, world) as E)
        entity.finalizeInit()

        return entity
    }

}