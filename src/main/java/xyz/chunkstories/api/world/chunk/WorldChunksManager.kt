package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldUser

interface WorldChunksManager {
    val world: World

    val allLoadedChunks: Sequence<Chunk>

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolderLocation(user: WorldUser, location: Location): ChunkHolder?

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolderWorldCoordinates(user: WorldUser, worldX: Int, worldY: Int, worldZ: Int): ChunkHolder?

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolder(user: WorldUser, chunkX: Int, chunkY: Int, chunkZ: Int): ChunkHolder?

    /** Returns true if a chunk was loaded. Not recommanded nor intended to use as a
     * replacement for a '== null' check after getChunk() because of the load/unload
     * mechanisms !  */
    fun isChunkLoaded(chunkX: Int, chunkY: Int, chunkZ: Int): Boolean

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk?

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunkWorldCoordinates(worldX: Int, worldY: Int, worldZ: Int): Chunk?

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunkWorldCoordinates(location: Location): Chunk?
}