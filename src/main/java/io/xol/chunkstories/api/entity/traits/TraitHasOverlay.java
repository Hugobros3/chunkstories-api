package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public abstract class TraitHasOverlay extends Trait {

	public TraitHasOverlay(Entity entity) {
		super(entity);
	}
	
	public abstract void drawEntityOverlay(RenderingInterface renderingInterface);

}
