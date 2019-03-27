//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import org.joml.Vector3dc
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.cell.CellComponents
import xyz.chunkstories.api.world.cell.DummyCell
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

    override val isAirChunk: Boolean
        get() = true

    override val entitiesWithinChunk: Collection<Entity>
        get() = emptySet()

    override val holder: ChunkHolder
        get() = throw UnsupportedOperationException("holder()")

    override val lightBaker: ChunkLightUpdater
        get() = throw UnsupportedOperationException()

    override val mesh: ChunkMesh
        get() = throw UnsupportedOperationException()

    override val occlusion: ChunkOcclusionManager
        get() = throw UnsupportedOperationException()

    override fun destroy() {}

    override fun peek(location: Vector3dc): ChunkCell {
        return peek(location.x().toInt(), location.y().toInt(), location.z().toInt())
    }

    override fun peek(x: Int, y: Int, z: Int): ChunkCell {
        return DummyChunkCell(this, x, y, z)
    }

    internal inner class DummyChunkCell(dummyChunk: DummyChunk, x: Int, y: Int, z: Int) : DummyCell(x, y, z, null!!, 0, 0, 0), ChunkCell {

        override// TODO Auto-generated method stub
        val chunk: Chunk
            get() = this@DummyChunk

        override val components: CellComponents
            get() = throw UnsupportedOperationException("components()")

        override fun refreshRepresentation() {

        }
    }

    override fun peekSimple(x: Int, y: Int, z: Int): Voxel {
        throw UnsupportedOperationException()
    }

    override fun getComponentsAt(worldX: Int, worldY: Int, worldZ: Int): CellComponents {
        throw UnsupportedOperationException("components()")
    }

    override fun addEntity(entity: Entity) {
        throw UnsupportedOperationException("addEntity()")
    }

    override fun removeEntity(entity: Entity) {
        throw UnsupportedOperationException("removeEntity()")
    }

    override fun peekRaw(x: Int, y: Int, z: Int): Int {
        // TODO Auto-generated method stub
        return 0
    }

    @Throws(WorldException::class)
    override fun poke(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int, cause: WorldModificationCause?): ChunkCell {
        throw UnsupportedOperationException()
    }

    override fun pokeSimple(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int) {
        throw UnsupportedOperationException()
    }

    override fun pokeSimpleSilently(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int) {
        throw UnsupportedOperationException()
    }

    override fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int) {
        throw UnsupportedOperationException()
    }

    override fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int) {
        throw UnsupportedOperationException()
    }
}
