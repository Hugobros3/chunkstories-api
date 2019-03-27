//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.voxel.Voxel

abstract class Cell(override val x: Int, override val y: Int, override val z: Int, override var voxel: Voxel, override var metaData: Int, override var blocklight: Int, override var sunlight: Int) : CellData {

    override fun toString(): String {
        return "[Cell $x, $y, $z $voxel $sunlight:$blocklight:$metaData]"
    }
}
