//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.chunk.Chunk;

@SuppressWarnings("serial")
public abstract class ChunkException extends RegionException {

	private final Chunk chunk;
	
	public ChunkException(Chunk chunk) {
		super(chunk.getRegion());
		this.chunk = chunk;
	}

	public Chunk getChunk() {
		return chunk;
	}
	
}
