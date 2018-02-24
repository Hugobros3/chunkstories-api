//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.server;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.PluginManager;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.WorldMaster;

public interface ServerInterface extends GameContext
{
	/** Returns the (public) IP the server is joinable at */
	public String getPublicIp();
	
	/** Returns how many seconds this server has been running for */
	public long getUptime();
	
	public UserPrivileges getUserPrivileges();
	
	/** @return The players that are <b>logged in</b> */
	public IterableIterator<Player> getConnectedPlayers();
	
	public int getConnectedPlayersCount();

	public Player getPlayerByName(String string);
	
	public Player getPlayerByUUID(long UUID);

	public PluginManager getPluginManager();

	/** Obtains the current permissions manager */
	public PermissionsManager getPermissionsManager();
	
	/** Installs a custom permissions manager */
	public void installPermissionsManager(PermissionsManager permissionsManager);
	
	public void broadcastMessage(String message);

	public WorldMaster getWorld();
	
	public void reloadConfig();
}
