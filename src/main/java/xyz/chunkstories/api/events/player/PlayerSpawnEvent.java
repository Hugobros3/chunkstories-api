//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player;

import xyz.chunkstories.api.Location;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.world.WorldMaster;

import javax.annotation.Nullable;

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

	@Nullable
	private Entity entity;
	@Nullable
	private Location spawnLocation;

	public PlayerSpawnEvent(Player player, WorldMaster world, @Nullable Entity entity, @Nullable Location spawnLocation) {
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

	/** By default the entity is loaded from the players/[username].csf file if it
	 * exists, else it's null. If no entity is set by a third-party plugin, a
	 * default one will be provided */
	@Nullable
	public Entity getEntity() {
		return entity;
	}

	/** Sets the entity to spawn the player as */
	public void setEntity(@Nullable Entity entity) {
		this.entity = entity;
	}

	@Nullable
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(@Nullable Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

}