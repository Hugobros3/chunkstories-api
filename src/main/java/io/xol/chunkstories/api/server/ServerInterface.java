package io.xol.chunkstories.api.server;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.PluginManager;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.WorldMaster;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface ServerInterface extends GameContext
{
	/** Returns the (public) IP the server is joinable at */
	public String getPublicIp();
	
	/** Returns how many seconds this server has been running for */
	public long getUptime();
	
	public UserPrivileges getUserPrivileges();
	
	/** Returns players <b>logged in</b> */
	public ConnectedPlayers getConnectedPlayers();
	
	interface ConnectedPlayers extends IterableIterator<Player> {
		/** Returns how many players are connected, might change over time, this number is unrelated to the iterator state */
		public int count();
	}

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
