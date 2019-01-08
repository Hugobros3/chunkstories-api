//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.input.Input;

public abstract class TraitInteractible extends Trait {

	public TraitInteractible(Entity entity) {
		super(entity);
	}

	public abstract boolean handleInteraction(Entity entity, Input input);
}
