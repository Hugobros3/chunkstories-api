//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.world.World;

/** World streaming packets are treated differently too. */
public abstract class PacketWorldStreaming extends Packet {
	
	protected final World world;
	
	public PacketWorldStreaming(World world)
	{
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
}
