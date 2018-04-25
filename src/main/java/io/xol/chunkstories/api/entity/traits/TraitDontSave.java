package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;

/** Any entity with this trait won't be saved in the chunks data */
public class TraitDontSave extends Trait {

	public TraitDontSave(Entity entity) {
		super(entity);
	}

}
