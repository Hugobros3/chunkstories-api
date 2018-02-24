//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.player;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.player.Player;

public class PlayerDeathEvent extends Event
{
	// Every event class has to have this
	
	static EventListeners listeners = new EventListeners(PlayerDeathEvent.class);
	
	@Override
	public EventListeners getListeners()
	{
		return listeners;
	}
	
	public static EventListeners getListenersStatic()
	{
		return listeners;
	}
	
	// Specific event code
	
	final Player player;
	String deathMessage;

	public PlayerDeathEvent(Player player)
	{
		this.player = player;
		this.deathMessage = player.getDisplayName()+" died.";
	}
	
	public String getDeathMessage()
	{
		return deathMessage;
	}

	public void setDeathMessage(String deathMessage)
	{
		this.deathMessage = deathMessage;
	}

	public Player getPlayer()
	{
		return player;
	}
}
