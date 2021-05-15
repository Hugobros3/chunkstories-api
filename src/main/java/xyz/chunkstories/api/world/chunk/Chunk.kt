//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.block.BlockAdditionalData
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.world.MutableWorldCell
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldCell
import xyz.chunkstories.api.world.cell.CellData
import xyz.chunkstories.api.world.region.Region

interface ChunkCell : WorldCell {
    val chunk: Chunk
    val additionalData: Map<String, BlockAdditionalData>
}

interface MutableChunkCell : ChunkCell, MutableWorldCell {
    fun registerAdditionalData(name: String, data: BlockAdditionalData)
    fun unregisterAdditionalData(name: String): Boolean
}

/** Contains 32x32x32 voxels worth of data  */
interface Chunk {
    val world: World
    val region: Region

    // TODO rename these
    val chunkX: Int
    val chunkY: Int
    val chunkZ: Int

    val entitiesWithinChunk: Collection<Entity>

    val holder: ChunkHolder

    fun getCell(x: Int, y: Int, z: Int): ChunkCell
    fun getCellMut(x: Int, y: Int, z: Int): MutableChunkCell

    fun getCellData(x: Int, y: Int, z: Int): CellData
    fun setCellData(x: Int, y: Int, z: Int, data: CellData)
}