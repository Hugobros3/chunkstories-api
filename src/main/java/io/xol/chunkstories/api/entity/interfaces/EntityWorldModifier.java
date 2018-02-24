//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;

/** Gives an entity the power to modify blocks in the world */
public interface EntityWorldModifier extends Entity, WorldModificationCause{
	
}
