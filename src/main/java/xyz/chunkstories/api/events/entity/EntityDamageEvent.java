//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.entity;

import xyz.chunkstories.api.entity.DamageCause;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;

/** When a EntityLiving damage() method is called */
public class EntityDamageEvent extends CancellableEvent {
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
	final DamageCause cause;
	float damage;

	public EntityDamageEvent(Entity entity, DamageCause cause, float damage) {
		this.entity = entity;
		this.cause = cause;
		this.damage = damage;
	}

	public Entity getEntity() {
		return entity;
	}

	public DamageCause getDamageCause() {
		return cause;
	}

	public void setDamageDealt(float damage) {
		this.damage = damage;
	}

	public float getDamageDealt() {
		return damage;
	}
}
