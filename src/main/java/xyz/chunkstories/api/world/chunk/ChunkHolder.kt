//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.util.concurrency.Fence
import xyz.chunkstories.api.world.WorldUser
import xyz.chunkstories.api.world.region.Region

/** Regions have 8x8x8 slots for chunks, this holds the offline/compressed data
 * and takes care of loading/unloading the live data when required by users  */
interface ChunkHolder {
    /** @return The region this slot is in*/
    val region: Region

    val chunkX: Int
    val chunkY: Int
    val chunkZ: Int

    val state: ChunkHolder.State

    val users: Set<WorldUser>
    /** TODO */
    fun registerUser(user: WorldUser): Boolean
    /** TODO */
    fun unregisterUser(user: WorldUser): Boolean

    val chunk: Chunk?

    //TODO better
    fun compressChunkData()

    sealed class State {
        /** The initial state, loading chunks can't happen until the underlying region is done loading! */
        object WaitForRegionInitialLoad : State()

        /** When the users set is empty */
        object Unloaded : State()

        /** Transitioned to when an user is registered, transitions to Loaded */
        open class Loading(val fence: Fence) : State()

        /** Special case of loading */
        class Generating(fence: Fence) : Loading(fence)

        /** Chunk is in use and loaded */
        class Available(val chunk: Chunk) : State()
    }
}
