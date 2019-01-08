//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.events.Event;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.player.Player;

public class PlayerSelectItemEvent extends Event {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerSelectItemEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	public Player player;
	public Entity entity;
	public int newSlot;

	public PlayerSelectItemEvent(Player player, Entity playerEntity, int newSlot) {
		this.player = player;
		this.entity = playerEntity;
		this.newSlot = newSlot;
	}

	public Player getPlayer() {
		return player;
	}

}
