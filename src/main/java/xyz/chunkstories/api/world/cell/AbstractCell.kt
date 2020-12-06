//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.components.VoxelComponent

// Dumb POD structs for Cell stuff

open class PodCellData(override val blockType: Voxel,
                       override val sunlightLevel: Int,
                       override val blocklightLevel: Int,
                       override val extraData: Int,
                       override val additionalData: List<VoxelComponent>) : CellData

open class MutablePodCellData(override var blockType: Voxel,
                              override var sunlightLevel: Int,
                              override var blocklightLevel: Int,
                              override var extraData: Int,
                              override val additionalData: MutableList<VoxelComponent>) : MutableCellData

abstract class AbstractCell(override val x: Int, override val y: Int, override val z: Int, override val data: CellData) : Cell {
    override fun toString(): String {
        return "[Cell $x, $y, $z data=$data]"
    }
}

abstract class MutableAbstractCell(x: Int, y: Int, z: Int, override val data: MutableCellData) : AbstractCell(x, y, z, data)