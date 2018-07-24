//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.serializable;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.generic.TraitSerializableBoolean;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;

public class TraitCreativeMode extends TraitSerializableBoolean {
	public TraitCreativeMode(Entity entity) {
		super(entity);
	}

	public static final WorldModificationCause CREATIVE_MODE = new WorldModificationCause() {

		@Override
		public String getName() {
			return "Creative Mode";
		}

	};
}
