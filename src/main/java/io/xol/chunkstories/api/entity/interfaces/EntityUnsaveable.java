//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

/**
 * Some entity should not be saved in region files, like player-controlled entities
 */
public interface EntityUnsaveable// extends Entity
{
	public boolean shouldSaveIntoRegion();
}
