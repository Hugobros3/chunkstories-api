//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net;

import org.slf4j.Logger;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.world.World;

import javax.annotation.Nullable;

/** Processes received packets */
public interface PacketReceptionContext {

	/** Whoever are we sending packets to ? */
	public PacketSender getInterlocutor();

	public GameContext getContext();

	@Nullable
	public World getWorld();

	/** Returns the logger specific to packets processing events */
	public Logger logger();

	/** A fast way to tell if we are a server */
	public boolean isServer();
}
