package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** World packets are treated differently: they are effectively datagrams used to communicate about the state of the world */
public abstract class PacketWorld extends Packet {
	
	protected final World world;
	
	public PacketWorld(World world)
	{
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
}
