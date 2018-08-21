//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.entity;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;

/** This event is called upon confirmed death of a living entity. You can't and
 * shouldn't prevent it from dying here, instead use the EntityDamageEvent to
 * cancel the damage. */
public class EntityDeathEvent extends Event {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(EntityDeathEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	Entity entity;

	public EntityDeathEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
