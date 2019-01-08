//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.physics.EntityHitbox;

/** Any entity exposing a children of that trait will be considered having
 * hitboxes */
public abstract class TraitHitboxes extends Trait {

	public TraitHitboxes(Entity entity) {
		super(entity);
	}

	public abstract EntityHitbox[] getHitBoxes();
}
