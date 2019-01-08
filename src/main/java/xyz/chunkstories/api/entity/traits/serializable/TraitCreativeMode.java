//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.generic.TraitSerializableBoolean;
import xyz.chunkstories.api.events.voxel.WorldModificationCause;

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
