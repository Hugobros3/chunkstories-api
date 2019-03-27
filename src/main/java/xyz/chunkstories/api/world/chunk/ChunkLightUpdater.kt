//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.util.concurrency.Fence

interface ChunkLightUpdater {
    /** Increments the needed updates counter, spawns a task if none exists or is
     * pending execution and gives you a fence to wait on.  */
    fun requestUpdateAndGetFence(): Fence

    /** Increments the needed updates counter, spawns a task if none exists or is
     * pending execution  */
    fun requestUpdate()

    /// ** Returns how many updates have yet to be done */
    // public int getPendingUpdates();
}