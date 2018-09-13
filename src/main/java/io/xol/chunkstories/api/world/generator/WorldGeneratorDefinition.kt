package io.xol.chunkstories.api.world.generator

import io.xol.chunkstories.api.content.Definition
import io.xol.chunkstories.api.world.World

class WorldGeneratorDefinition(name: String, properties: Map<String, String>) : Definition(name, properties) {
    fun createForWorld(world : World) {
        //TODO
    }
}