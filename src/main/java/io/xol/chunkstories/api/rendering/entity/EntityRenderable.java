//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.entity.Entity;

public interface EntityRenderable extends Entity
{
	/**
	 * An EntityRenderable provides a EntityRenderer object to batch-render all entities of the same type
	 */
	public EntityRenderer<? extends EntityRenderable> getEntityRenderer();
}
