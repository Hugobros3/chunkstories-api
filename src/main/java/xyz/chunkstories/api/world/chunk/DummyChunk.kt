//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.region.Region

/** A fake chunk, for all the non-reality fans Also, might be usefull when
 * dealing with blocks all by themselves.  */
class DummyChunk : Chunk {
    override val world: World
        get() = throw UnsupportedOperationException()

    override val region: Region
        get() = throw UnsupportedOperationException()

    override val chunkX: Int
        get() = 0

    override val chunkY: Int
        get() = 0

    override val chunkZ: Int
        get() = 0

    override val entitiesWithinChunk: Collection<Entity>
        get() = emptySet()

    override val holder: ChunkHolder
        get() = throw UnsupportedOperationException("holder()")

    override fun getCell(x: Int, y: Int, z: Int): ChunkCell {
        TODO("Not yet implemented")
    }

    override fun getCellMut(x: Int, y: Int, z: Int): MutableChunkCell {
        TODO("Not yet implemented")
    }
}
