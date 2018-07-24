//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.chunk;

import io.xol.chunkstories.api.util.concurrency.Fence;

public interface ChunkLightUpdater {

	/** Increments the needed updates counter but doesn't spawn a task */
	public void incrementPendingUpdates();

	/**
	 * Increments the needed updates counter, spawns a task if none exists or is
	 * pending execution
	 */
	public Fence requestLightningUpdate();

	/**
	 * Spawns a TaskLightChunk if there are unbaked modifications and no task is
	 * pending execution
	 */
	public void spawnUpdateTaskIfNeeded();

	/** Returns how many light updates have yet to be done */
	public int pendingUpdates();
}