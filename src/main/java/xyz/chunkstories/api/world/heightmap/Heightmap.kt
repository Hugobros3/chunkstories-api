//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.heightmap

import xyz.chunkstories.api.block.BlockType
import xyz.chunkstories.api.util.concurrency.Fence
import xyz.chunkstories.api.world.WorldUser
import xyz.chunkstories.api.world.cell.Cell

/** Represents the topmost cell for 256 * 256 area. X and Z coordinates match those of regions  */
interface Heightmap {
    /** Get the region-space X coordinate for the begining of this heightmap data  */
    val regionX: Int
    /** Get the region-space Z coordinate for the begining of this heightmap data  */
    val regionZ: Int

    val state: State

    val users: Set<WorldUser>
    fun unregisterUser(user: WorldUser): Boolean

    /** Return the height of the topmost block or -1 if unknown */
    fun getHeight(x: Int, z: Int): Int
    fun getBlockType(x: Int, z: Int): BlockType
    //fun set(cell: Cell)

    sealed class State {
        open class Loading(val fence: Fence) : State()

        class Generating(fence: Fence) : Loading(fence)

        open class Available : State()

        class Saving(val fence: Fence) : Available()

        object Zombie : State()
    }
}
