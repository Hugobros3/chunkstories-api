//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client.net;

import io.xol.chunkstories.api.client.ClientInterface;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.WorldClient;

public interface ClientPacketsProcessor extends PacketReceptionContext {
	
	public ClientInterface getContext();
	
	public WorldClient getWorld();
	
	public Player getPlayer();
}
