//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;

public class EntityCreativeMode extends EntityComponentGenericBoolean {
	public EntityCreativeMode(Entity entity) {
		super(entity);
	}

	public static final WorldModificationCause CREATIVE_MODE = new WorldModificationCause() {

		@Override
		public String getName()
		{
			return "Creative Mode";
		}
		
};
}
