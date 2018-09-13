//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.Definition
import io.xol.chunkstories.api.util.kotlin.initOnce
import io.xol.chunkstories.api.world.World
import java.lang.reflect.Constructor

class EntityDefinition(val store: Content.EntityDefinitions, name: String, properties: Map<String, String>) : Definition(name, properties) {
    /** When added to the game content, either by being loaded explicitly or programatically, will be set to an integer
     * value. Attempting to manually override/set this identifier yourself will result in a house fire. */
    var assignedId : Int by initOnce()

    /** Shorthand for Java accesses */
    fun store() = store

    val clazz: Class<Entity>
    private val constructor: Constructor<Entity>

    init {
        clazz = this.resolveProperty("class")?.let {
            store.parent().modsManager().getClassByName(it)?.let {
                if(Entity::class.java.isAssignableFrom(it))
                    it as Class<Entity>
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
        val entity = (constructor.newInstance(this, world)!! as E)!!
        entity.afterIntialization()

        return entity
    }

}