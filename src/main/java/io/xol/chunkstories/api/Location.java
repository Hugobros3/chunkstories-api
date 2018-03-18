//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.world.World;

/** Cartesian product of a world and a position within that world */
public class Location extends Vector3d {
	protected World world;

	public Location(World world, double x, double y, double z) {
		super(x, y, z);
		this.world = world;
	}

	public Location(World world, Vector3dc position) {
		this(world, position.x(), position.y(), position.z());
	}

	public Location(Location location) {
		this.world = location.getWorld();
		this.x = (location.x);
		this.y = (location.y);
		this.z = (location.z);
	}

	public World getWorld() {
		return world;
	}
}
