package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

@SuppressWarnings("serial")
public abstract class WorldException extends Exception {
	public final World world;
	
	public WorldException(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
}
