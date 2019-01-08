//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client.net;

import xyz.chunkstories.api.client.Client;
import xyz.chunkstories.api.client.IngameClient;
import xyz.chunkstories.api.client.LocalPlayer;
import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.world.WorldClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ClientPacketsProcessor extends PacketReceptionContext {
	public IngameClient getContext();

	@Nonnull
	public WorldClient getWorld();

	@Nonnull
	public LocalPlayer getPlayer();
}
