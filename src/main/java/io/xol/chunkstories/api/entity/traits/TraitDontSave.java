//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;

/** Any entity with this trait won't be saved in the chunks data */
public class TraitDontSave extends Trait {

	public TraitDontSave(Entity entity) {
		super(entity);
	}

}
