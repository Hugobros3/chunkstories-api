//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.player;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.CancellableEvent;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.WorldMaster;

/** Player (re) spawn event - Triggered when a player is spawned into a world */
public class PlayerSpawnEvent extends CancellableEvent {
	// Every event class has to have this
	static EventListeners listeners = new EventListeners(PlayerSpawnEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	private final Player player;
	private final WorldMaster world;

	private Entity entity;
	private Location spawnLocation;

	public PlayerSpawnEvent(Player player, WorldMaster world, Entity entity, Location spawnLocation) {
		this.player = player;
		this.world = world;

		this.entity = entity;
		this.spawnLocation = spawnLocation;
	}

	public Player getPlayer() {
		return player;
	}

	public WorldMaster getWorld() {
		return world;
	}

	/**
	 * By default the entity is loaded from the players/[username].csf file if it
	 * exists, else it's null. If no entity is set by a third-party plugin, a
	 * default one will be provided
	 */
	public Entity getEntity() {
		return entity;
	}

	/** Sets the entity to spawn the player as */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

}
