package io.xol.chunkstories.api.world.generator

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.Definition
import io.xol.chunkstories.api.world.World
import java.lang.reflect.Constructor

class WorldGeneratorDefinition(val store: Content.WorldGenerators, name: String, properties: Map<String, String>) : Definition(name, properties) {
    val clazz: Class<WorldGenerator>
    private val constructor: Constructor<WorldGenerator>

    init {
        clazz = this.resolveProperty("class")?.let {
            store.parent().modsManager().getClassByName(it)?.let {
                if (WorldGenerator::class.java.isAssignableFrom(it))
                    it as Class<WorldGenerator>
                else
                    throw Exception("The custom class has to extend the WorldGenerator class !")
            }
        } ?: WorldGenerator::class.java

        constructor = try {
            clazz.getConstructor(World::class.java)
        } catch (e: NoSuchMethodException) {
            throw Exception("Your custom class, $clazz, lacks the correct WorldGenerator(World) constructor.")
        }

    }

    fun <WG : WorldGenerator> createForWorld(world: World): WG = constructor.newInstance(world) as WG
}