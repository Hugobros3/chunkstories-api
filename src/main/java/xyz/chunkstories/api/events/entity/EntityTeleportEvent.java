//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.entity;

import xyz.chunkstories.api.Location;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;

public class EntityTeleportEvent extends CancellableEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(EntityDamageEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	final Entity entity;
	final Location newLocation;

	public EntityTeleportEvent(Entity entity, Location newLocation) {
		this.entity = entity;
		this.newLocation = newLocation;
	}

	public Entity getEntity() {
		return entity;
	}

	public Location getNewLocation() {
		return newLocation;
	}
}
