//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.generator

import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.chunk.Chunk

/** The job of a WorldGenerator is to newEntity (voxel) data and to populate the
 * world with content. It also has duties of providing some rendering hints on
 * the world  */
abstract class WorldGenerator(val definition: WorldGeneratorDefinition, protected val world: World) {

    abstract fun generateWorldSlice(chunks: Array<Chunk>)
}
