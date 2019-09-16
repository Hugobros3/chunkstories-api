//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.generator

import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.chunk.Chunk

class BlankWorldGenerator(type: WorldGeneratorDefinition, world: World) : WorldGenerator(type, world) {

    override fun generateWorldSlice(chunks: Array<Chunk>) {
        /*for (chunkY in chunks.indices) {
            generateChunk(chunks[chunkY])
        }*/
    }
}
