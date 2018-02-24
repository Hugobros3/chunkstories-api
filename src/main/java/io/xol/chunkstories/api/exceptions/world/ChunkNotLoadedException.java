//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.chunk.Region;

public class ChunkNotLoadedException extends RegionException {

	private static final long serialVersionUID = -451869658162736185L;
	private final int chunkX, chunkY, chunkZ;

	public ChunkNotLoadedException(Region region, int chunkX, int chunkY, int chunkZ) {
		super(region);
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	@Override
	public String getMessage() {
		if(getRegion() == null)
			return "Chunk at " + chunkX + ":" + chunkY + ":" + chunkZ + "was not loaded";
		else
			return "Chunk at " + chunkX + ":" + chunkY + ":" + chunkZ + "was not loaded (but the underlying region was)";
	}

	
}
