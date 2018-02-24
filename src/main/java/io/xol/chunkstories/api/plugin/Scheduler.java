//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

/** Quick and dirty interface to reproduce Bukkit scheduler functionality for the time being */
public interface Scheduler
{
	public void scheduleSyncRepeatingTask(ChunkStoriesPlugin p, Runnable runnable, long l, long m);
}
