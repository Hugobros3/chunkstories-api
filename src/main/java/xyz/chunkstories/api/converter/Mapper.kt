//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.converter

import io.xol.enklume.MinecraftRegion
import xyz.chunkstories.api.block.BlockType
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.cell.MutableCellData

/** Is used to write out the corresponding voxel type to a Minecraft block  */
abstract class Mapper(protected val blockType: BlockType) {
    abstract fun output(minecraftId: Int, minecraftMeta: Byte, output: MutableCellData)
}

/** For blocks that have some fancy properties we need to consider, like doors or signs. */
abstract class NonTrivialMapper(blockType: BlockType?) : Mapper(blockType!!) {
    override fun output(minecraftId: Int, minecraftMeta: Byte, output: MutableCellData) {
        throw UnsupportedOperationException()
    }

    /** Responsible of re-building the input block in chunkstories-space.  */
    abstract fun output(csWorld: World, csX: Int, csY: Int, csZ: Int,
                        minecraftBlockId: Int, minecraftMetaData: Int,
                        region: MinecraftRegion,
                        minecraftCuurrentChunkXinsideRegion: Int,
                        minecraftCuurrentChunkZinsideRegion: Int,
                        x: Int, y: Int, z: Int)
}