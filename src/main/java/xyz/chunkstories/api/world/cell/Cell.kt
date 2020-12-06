//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.components.VoxelComponent

interface CellData {
    val blockType: Voxel

    val sunlightLevel: Int
    val blocklightLevel: Int

    /** Warning: only the 8 lower bits are used/saved */
    val extraData: Int
    val additionalData: List<VoxelComponent>
}

interface MutableCellData : CellData {
    override var blockType: Voxel

    override var sunlightLevel: Int
    override var blocklightLevel: Int

    override var extraData: Int
    override val additionalData: MutableList<VoxelComponent>
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
        val voxel = data.blockType
        return voxel.getTranslatedCollisionBoxes(this)
    }