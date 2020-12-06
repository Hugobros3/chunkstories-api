//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server;

import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.world.WorldMaster;

public interface ServerPacketsProcessor {
	public Host getContext();

	public WorldMaster getWorld();

	/** Players each have a subclass of this interface */
	public interface ServerPlayerPacketsProcessor extends PacketReceptionContext, ServerPacketsProcessor {
		public Player getPlayer();
	}
}