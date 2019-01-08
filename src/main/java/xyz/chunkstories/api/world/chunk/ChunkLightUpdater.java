//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk;

import xyz.chunkstories.api.util.concurrency.Fence;

public interface ChunkLightUpdater {
	/** Increments the needed updates counter, spawns a task if none exists or is
	 * pending execution and gives you a fence to wait on. */
	public Fence requestUpdateAndGetFence();

	/** Increments the needed updates counter, spawns a task if none exists or is
	 * pending execution */
	public void requestUpdate();

	/// ** Returns how many updates have yet to be done */
	// public int getPendingUpdates();
}