//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client.net;

import io.xol.chunkstories.api.client.Client;
import io.xol.chunkstories.api.client.IngameClient;
import io.xol.chunkstories.api.client.LocalPlayer;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.WorldClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ClientPacketsProcessor extends PacketReceptionContext {
	public IngameClient getContext();

	@Nonnull
	public WorldClient getWorld();

	@Nonnull
	public LocalPlayer getPlayer();
}
