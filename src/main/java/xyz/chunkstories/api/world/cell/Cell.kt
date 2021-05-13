//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.block.BlockType

interface CellData {
    val blockType: BlockType

    val sunlightLevel: Int
    val blocklightLevel: Int

    /** Warning: only the 8 lower bits are used/saved */
    val extraData: Int
}

interface MutableCellData : CellData {
    override var blockType: BlockType

    override var sunlightLevel: Int
    override var blocklightLevel: Int

    override var extraData: Int
}

interface Cell {
    val x: Int
    val y: Int
    val z: Int

    val data: CellData
}

interface MutableCell : Cell {
    override val data: MutableCellData
}

/** Returns an array (possibly 0-sized) of collision boxes translated to the actual position of the block */
val Cell.translatedCollisionBoxes: Array<Box>
    get() {
        val blockType = data.blockType
        return blockType.getTranslatedCollisionBoxes(this)
    }