//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin

/** Quick and dirty interface to reproduce Bukkit scheduler functionality for
 * the time being  */
interface Scheduler {
    fun scheduleSyncRepeatingTask(p: ChunkStoriesPlugin, runnable: Runnable, l: Long, m: Long)
}
