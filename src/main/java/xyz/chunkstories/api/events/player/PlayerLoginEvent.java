//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player;

import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.events.categories.PlayerEvent;
import xyz.chunkstories.api.player.Player;

public class PlayerLoginEvent extends CancellableEvent implements PlayerEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerLoginEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	public String getConnectionMessage() {
		return connectionMessage;
	}

	public void setConnectionMessage(String connectionMessage) {
		this.connectionMessage = connectionMessage;
	}

	public String getRefusedConnectionMessage() {
		return refusedConnectionMessage;
	}

	public void setRefusedConnectionMessage(String refusedConnectionMessage) {
		this.refusedConnectionMessage = refusedConnectionMessage;
	}

	private Player player;
	private String connectionMessage;
	private String refusedConnectionMessage = "Connection was refused by a plugin.";

	public PlayerLoginEvent(Player player) {
		this.player = player;
		this.connectionMessage = "#FFFF00" + player + " joined the server";
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
