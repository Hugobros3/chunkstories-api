package io.xol.chunkstories.api.world.chunk;

import io.xol.chunkstories.api.util.concurrency.Fence;

public interface ChunkLightUpdater {

	/** Increments the needed updates counter, spawns a task if none exists or is pending execution */
	Fence requestLightningUpdate();

	/** Spawns a TaskLightChunk if there are unbaked modifications and no task is pending execution */
	void spawnUpdateTaskIfNeeded();

	/** Returns how many light updates have yet to be done */
	int pendingUpdates();

}