//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.world.World

/** Represents the data for a future state for a cell. */
open class FutureCell @JvmOverloads constructor(override val world: World, x: Int, y: Int, z: Int, voxel: Voxel, meta: Int = -1, blocklight: Int = -1, sunlight: Int = -1) : Cell(x, y, z, voxel, meta, blocklight, sunlight), CellData, EditableCell {

    val data: Int
        @Deprecated("")
        get() = VoxelFormat.format(world.contentTranslator.getIdForVoxel(voxel), metaData, sunlight, blocklight)

    //TODO WorldCell
    constructor(ogContext: CellData) : this(ogContext.world, ogContext.x, ogContext.y, ogContext.z, ogContext.voxel, ogContext.metaData, ogContext.blocklight, ogContext.sunlight)

    var overwriteVoxel: Boolean

    init {
        // By default air is not going to overwrite other blocks
        overwriteVoxel = voxel.isAir()
    }

    /** The voxel to write.
     * If the 'overwriteVoxel' flag is set to false this will be ignored.
     * If this is set explicitely to any voxel type the 'overwriteVoxel' flag will be set to true.
     */
    override var voxel: Voxel
        get() = super.voxel
        set(value) {
            super.voxel = value
            overwriteVoxel = true
        }

    @Deprecated("")
    fun getNeightborData(side: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun getNeightbor(side: Int): CellData {
        throw UnsupportedOperationException("getNeightbor()")
    }
}
