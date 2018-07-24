//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.content.Content.EntityDefinitions;

public interface EntityDefinition extends Definition {
	public String getName();

	public Entity create(Location location);

	/**
	 * Returns true if this entity should collide with others ( can be used to boost
	 * for instance )
	 */
	public boolean collidesWithEntities();

	public EntityDefinitions store();
}
