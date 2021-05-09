//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.generator

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.world.World
import java.lang.reflect.Constructor

class WorldGeneratorDefinition(val store: Content.WorldGenerators, val name: String, val properties: Json.Dict) {
    val clazz: Class<WorldGenerator>
    private val constructor: Constructor<WorldGenerator>

    init {
        clazz = properties["class"].asString?.let {
            store.parent.modsManager.getClassByName(it)?.let {
                if (WorldGenerator::class.java.isAssignableFrom(it))
                    it as Class<WorldGenerator>
                else
                    throw Exception("The custom class has to extend the WorldGenerator class !")
            }
        } ?: WorldGenerator::class.java

        constructor = try {
            clazz.getConstructor(WorldGeneratorDefinition::class.java, World::class.java)
        } catch (e: NoSuchMethodException) {
            throw Exception("Your custom class, $clazz, lacks the correct WorldGenerator(WorldGeneratorDefinition, World) constructor.")
        }

    }

    fun <WG : WorldGenerator> createForWorld(world: World): WG = constructor.newInstance(this, world) as WG
}