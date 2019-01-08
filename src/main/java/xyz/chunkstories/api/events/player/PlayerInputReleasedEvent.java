//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player;

import xyz.chunkstories.api.events.Event;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.input.Input;
import xyz.chunkstories.api.player.Player;

public class PlayerInputReleasedEvent extends Event {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerInputReleasedEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	Player player;
	Input input;

	public PlayerInputReleasedEvent(Player player, Input input) {
		this.player = player;
		this.input = input;
	}

	public Player getPlayer() {
		return player;
	}

	public Input getInput() {
		return input;
	}
}
