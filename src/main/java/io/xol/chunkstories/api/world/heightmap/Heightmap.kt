//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.heightmap

import io.xol.chunkstories.api.util.concurrency.Fence
import io.xol.chunkstories.api.world.WorldUser
import io.xol.chunkstories.api.world.cell.CellData
import io.xol.chunkstories.api.world.region.Region

/** Represents the topmost getCell for 256 * 256 area. X and Z coordinates match
 * those of regions  */
interface Heightmap {
    /** Get the region-space X coordinate for the begining of this heightmap data  */
    val regionX: Int
    /** Get the region-space Z coordinate for the begining of this heightmap data  */
    val regionZ: Int

    val state: State

    val users: Set<WorldUser>
    fun registerUser(user: WorldUser): Boolean
    fun unregisterUser(user: WorldUser): Boolean

    /** Return the height of the topmost block or NO_DATA is no data is yet
     * available  */
    fun getHeight(x: Int, z: Int): Int

    fun getTopCell(x: Int, z: Int): CellData
    fun setTopCell(data: CellData)

    //fun save(): Fence

    sealed class State {
        open class Loading(val fence: Fence) : State()

        class Generating(fence: Fence) : Loading(fence)

        open class Available : State()
        class Saving(val fence: Fence) : Available()

        object Zombie : State()
    }

    companion object {
        val NO_DATA = -1
    }
}
