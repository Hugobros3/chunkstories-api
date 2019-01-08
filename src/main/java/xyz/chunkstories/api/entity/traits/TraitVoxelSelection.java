//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.Location;
import xyz.chunkstories.api.entity.Entity;

import javax.annotation.Nullable;

/** Any entity with this trait is able to select blocks */
public abstract class TraitVoxelSelection extends Trait {

	public TraitVoxelSelection(Entity entity) {
		super(entity);
	}

	/** Performs a raytrace and returns a location or null if out of bounds
	 * 
	 * @param inside Do we return the position where we collide or do we return the
	 *            empty space next to the face we hit ?
	 * @param overwriteInstaDestructibleBlocks Would we return a non-air getCell
	 *            provided the voxel in that getCell is instantaneously
	 *            destructible/replaceable ?
	 * @return */
	@Nullable
	public abstract Location getBlockLookingAt(boolean inside, boolean overwriteInstaDestructibleBlocks);
}