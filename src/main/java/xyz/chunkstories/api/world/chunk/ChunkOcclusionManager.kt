package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.util.concurrency.Fence

interface ChunkOcclusionManager {
    /** Increments the needed updates counter, spawns a task if none exists or is
     * pending execution and gives you a fence to wait on.  */
    fun requestUpdateAndGetFence(): Fence

    /** Increments the needed updates counter, spawns a task if none exists or is
     * pending execution  */
    fun requestUpdate()

    /// ** Returns how many updates have yet to be done */
    // public int getPendingUpdates();
}
