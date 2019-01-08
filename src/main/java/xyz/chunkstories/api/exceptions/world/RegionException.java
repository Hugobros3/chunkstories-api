//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world;

import xyz.chunkstories.api.world.region.Region;

@SuppressWarnings("serial")
public abstract class RegionException extends WorldException {

	private final Region region;

	public RegionException(Region region) {
		super(region.getWorld());
		this.region = region;
	}

	public Region getRegion() {
		return region;
	}
}
