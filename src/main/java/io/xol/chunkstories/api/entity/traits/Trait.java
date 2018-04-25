package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;

public abstract class Trait {
	public final Entity entity;

	public Trait(Entity entity) {
		this.entity = entity;
		entity.traits.registerTrait(this);
	}
}
