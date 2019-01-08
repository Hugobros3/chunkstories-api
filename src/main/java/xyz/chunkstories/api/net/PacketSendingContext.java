//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net;

import org.slf4j.Logger;

import xyz.chunkstories.api.GameContext;
import xyz.chunkstories.api.world.World;

/** Processes sent packets */
public interface PacketSendingContext {

	/** Whoever are we sending packets to ? */
	public PacketDestinator getInterlocutor();

	public GameContext getContext();

	public World getWorld();

	/** Returns the logger specific to packets processing events */
	public Logger logger();
}
