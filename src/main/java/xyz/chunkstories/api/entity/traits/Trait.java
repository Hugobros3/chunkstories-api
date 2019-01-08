//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.entity.Entity;

public abstract class Trait {
	public final int id;
	public final Entity entity;

	public Trait(Entity entity) {
		this.entity = entity;
		id = entity.traits.registerTrait(this);
	}

	public int id() {
		return id;
	}
}
