package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.chunk.Region;

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
