//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.world.World

/**
 * A special getCell outside of any physical realm; using id-relative or
 * world-relative methods will throw exceptions.
 */
open class DummyCell(x: Int, y: Int, z: Int, voxel: Voxel, meta: Int, blocklight: Int, sunlight: Int) : Cell(x, y, z, voxel, meta, blocklight, sunlight), CellData, EditableCell {

    override val world: World
        get() = throw UnsupportedOperationException()

    // Make up ids
    val data: Int
        @Deprecated("")
        get() = VoxelFormat.format(0xDEAD, metaData, sunlight, blocklight)

    override val location: Location
        get() = throw UnsupportedOperationException()

    override val translatedCollisionBoxes: Array<Box>?
        get() = throw UnsupportedOperationException()

    @Deprecated("")
    fun getNeightborData(side: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getNeightbor(side: Int): CellData {
        throw UnsupportedOperationException()
    }

    override fun getNeightborVoxel(side: Int): Voxel? {
        throw UnsupportedOperationException()
    }

    override fun getNeightborMetadata(side: Int): Int {
        throw UnsupportedOperationException()
    }
}
