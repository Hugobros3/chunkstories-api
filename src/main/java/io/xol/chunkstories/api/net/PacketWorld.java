//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.world.World;

/**
 * World packets are treated differently: they are effectively datagrams used to
 * communicate about the state of the world
 */
public abstract class PacketWorld extends Packet {

	protected final World world;

	public PacketWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}
}
