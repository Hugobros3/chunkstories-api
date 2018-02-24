//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.util.concurrency.Fence;

/**
 * A 'master' world is one hosting the game logic and who runs the 'serverside' plugins. It can be either a dedicated server or a singleplayer world.
 */
public interface WorldMaster extends World
{
	public void spawnPlayer(Player player);
	
	public IterableIterator<Player> getPlayers();
	
	public Player getPlayerByName(String playerName);
	
	/** Returns the folder where the world files are on disk. */
	public String getFolderPath();
	
	/** Stops the logic thread that calls the tick() method. Returns a fence that traverses once that thread is effecitvely dead. */
	public Fence stopLogic();
	
	/**
	 * Plays a soundEffect to all clients except once, typical use if sounds played locally by a player that can't suffer any lag for him
	 * but still need others to hear it as well
	 */
	//public void playSoundEffectExcluding(String soundEffect, Location location, float pitch, float gain, Subscriber subscriber);
}
