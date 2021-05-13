//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.block.BlockType

// Dumb POD structs for Cell stuff
open class PodCellData(override val blockType: BlockType,
                       override val sunlightLevel: Int = 15,
                       override val blocklightLevel: Int = 0,
                       override val extraData: Int = 0) : CellData

open class MutablePodCellData(override var blockType: BlockType,
                              override var sunlightLevel: Int = 15,
                              override var blocklightLevel: Int = 0,
                              override var extraData: Int = 0) : MutableCellData

open class PodCell(override val x: Int, override val y: Int, override val z: Int, override val data: CellData) : Cell {
    override fun toString(): String {
        return "[Cell $x, $y, $z data=$data]"
    }
}

open class MutablePodCell(x: Int, y: Int, z: Int, override val data: MutableCellData) : PodCell(x, y, z, data)