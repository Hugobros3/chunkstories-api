package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

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
