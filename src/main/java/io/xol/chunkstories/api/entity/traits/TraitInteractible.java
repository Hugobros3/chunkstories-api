//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.input.Input;

public abstract class TraitInteractible extends Trait {

	public TraitInteractible(Entity entity) {
		super(entity);
	}
	
	public abstract boolean handleInteraction(Entity entity, Input input);
}
