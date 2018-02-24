//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.server;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.WorldMaster;

public interface ServerPacketsProcessor {
	public ServerInterface getContext();
	
	public WorldMaster getWorld();
	
	/** Players each have a subclass of this interface */
	public interface ServerPlayerPacketsProcessor extends PacketReceptionContext, ServerPacketsProcessor {
		public Player getPlayer();
	}
}