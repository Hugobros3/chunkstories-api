//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.entity.EntityLiving;

public interface EntityFeedable extends EntityLiving {
	public float getFoodLevel();
	
	public void setFoodLevel(float value);
}
