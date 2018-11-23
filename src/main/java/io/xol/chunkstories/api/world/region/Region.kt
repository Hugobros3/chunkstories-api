//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.region

import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.util.concurrency.Fence
import io.xol.chunkstories.api.world.World
import io.xol.chunkstories.api.world.WorldUser
import io.xol.chunkstories.api.world.chunk.Chunk
import io.xol.chunkstories.api.world.chunk.ChunkHolder
import io.xol.chunkstories.api.world.heightmap.Heightmap

/** A region is 8x8x8 chunks and contains entities  */
interface Region {
    val world: World

    val regionX: Int
    val regionY: Int
    val regionZ: Int

    val state: Region.State

    /** Associated heightmap object, required by this region */
    val heightmap: Heightmap

    val entitiesWithinRegion: Sequence<Entity>

    /** WorldUsers keep the chunk loaded. The region is only kept in memory as long as this set is non-null. */
    val users: Set<WorldUser>
    fun registerUser(user: WorldUser): Boolean

    /** Unregisters an user.
     * In a master world, unregistering the last user will cause a transition to the Saving state.
     * In a remote world, unregistering the last user will directly transition to the Zombie state. */
    fun unregisterUser(user: WorldUser): Boolean

    val loadedChunks: Sequence<Chunk>
    fun getChunkHolder(chunkX: Int, chunkY: Int, chunkZ: Int): ChunkHolder
    fun getChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk?
    //fun isChunkLoaded(chunkX: Int, chunkY: Int, chunkZ: Int): Boolean

    /** Will be traversable once the file representing the region at the time of
     * calling this is done writing.  */
    // fun save(): Fence

    sealed class State {
        /** Waiting on the compressed/serialized region data to be available. */
        open class Loading(val fence: Fence) : State()

        /** Special case of loading, we're generating this region for the first time */
        class Generating(fence: Fence) : Loading(fence)

        /** Region is properly loaded and can load chunks within it.*/
        open class Available : State()

        /** Region has no users but it's currently doing save operations and will be kept in memory until those complete.
         * If an user registers to this region before the save operation is done, the region will transition back to the Available state. */
        class Saving(val fence: Fence) : Available()

        /** Region was unloaded because of lack of users and this object is to be discarded.
         * Regions objects can't go back from this state, you need to acquire them back from World. */
        object Zombie : State()
    }
}