//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.player;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.events.categories.PlayerEvent;
import io.xol.chunkstories.api.player.Player;

public class PlayerLogoutEvent extends Event implements PlayerEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerLogoutEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	private Player player;
	private String logoutMessage;

	public PlayerLogoutEvent(Player player) {
		this.player = player;
		this.logoutMessage = "#FFFF00" + player + " left the server";
	}

	public String getLogoutMessage() {
		return logoutMessage;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
