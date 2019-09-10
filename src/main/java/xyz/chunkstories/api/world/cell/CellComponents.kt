//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import kotlin.collections.Map.Entry

import xyz.chunkstories.api.util.IterableIterator
import xyz.chunkstories.api.voxel.components.VoxelComponent
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.chunk.Chunk
import xyz.chunkstories.api.world.chunk.ChunkCell

/** Represents the various VoxelComponents that may exist in one voxel cell */
interface CellComponents {
    val chunk: Chunk

    val world: World

    /** Returns the WORLD x coordinate  */
    val x: Int

    /** Returns the WORLD y coordinate  */
    val y: Int

    /** Returns the WORLD z coordinate  */
    val z: Int

    /** Peeks the cell containing those components  */
    val cell: ChunkCell

    val allVoxelComponents: Collection<Entry<String, VoxelComponent>>

    /// ** Returns a list of users that can see this cell */
    // public IterableIterator<WorldUser> users();

    fun getVoxelComponent(name: String): VoxelComponent?

    /** Looks for a VoxelComponent and returns it's name if it is contained in this
     * cell.  */
    fun getRegisteredComponentName(component: VoxelComponent): String?
}
