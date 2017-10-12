package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponentVelocity;

public interface EntityWithVelocity extends Entity {
	
	public EntityComponentVelocity getVelocityComponent();
}
