package io.xol.chunkstories.api.net;

import org.slf4j.Logger;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Processes sent packets */
public interface PacketSendingContext {

	/** Whoever are we sending packets to ? */
	public PacketDestinator getInterlocutor();
	
	public GameContext getContext();
	
	public World getWorld();

	/** Returns the logger specific to packets processing events */
	public Logger logger();
}
