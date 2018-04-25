package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.physics.EntityHitbox;

/** Any entity exposing a children of that trait will be considered having hitboxes */
public abstract class TraitHitboxes extends Trait {

	public TraitHitboxes(Entity entity) {
		super(entity);
	}
	
	public abstract EntityHitbox[] getHitBoxes();
}
