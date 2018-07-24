//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.world;

import io.xol.chunkstories.api.world.World;

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
