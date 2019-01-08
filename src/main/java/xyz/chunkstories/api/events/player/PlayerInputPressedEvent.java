//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player;

import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.input.Input;
import xyz.chunkstories.api.player.Player;

public class PlayerInputPressedEvent extends CancellableEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerInputPressedEvent.class);

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

	public PlayerInputPressedEvent(Player player, Input input) {
		this.player = player;
		this.input = input;
	}

	public Input getInput() {
		return input;
	}

	public Player getPlayer() {
		return player;
	}
}
