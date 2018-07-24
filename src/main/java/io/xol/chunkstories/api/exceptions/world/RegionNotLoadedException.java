//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.World;

public class RegionNotLoadedException extends WorldException {

	private static final long serialVersionUID = -451869658162736185L;
	private final int regionX, regionY, regionZ;

	public RegionNotLoadedException(World world, int regionX, int regionY, int regionZ) {
		super(world);
		this.regionX = regionX;
		this.regionY = regionY;
		this.regionZ = regionZ;
	}

	public int getRegionX() {
		return regionX;
	}

	public int getRegionY() {
		return regionY;
	}

	public int getRegionZ() {
		return regionZ;
	}

	@Override
	public String getMessage() {
		return "Region at " + regionX + ":" + regionY + ":" + regionZ + "was not loaded";
	}

}
