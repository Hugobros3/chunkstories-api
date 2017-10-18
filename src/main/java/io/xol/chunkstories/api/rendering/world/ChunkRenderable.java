package io.xol.chunkstories.api.rendering.world;

import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.chunk.Chunk;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface ChunkRenderable extends Chunk
{
	public ChunkMeshUpdater meshUpdater();
	
	public interface ChunkMeshUpdater {
		/** Increments the needed updates counter, spawns a task if none exists or is pending execution */
		Fence requestMeshUpdate();

		/** Spawns a TaskLightChunk if there are unbaked modifications and no task is pending execution */
		void spawnUpdateTaskIfNeeded();

		/** Returns how many light updates have yet to be done */
		int pendingUpdates();
	}
}
