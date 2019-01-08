//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell;

import xyz.chunkstories.api.Location;
import xyz.chunkstories.api.physics.Box;
import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.World;

import javax.annotation.Nullable;

/** A CellData is representing the content of a specific getCell in the world.
 * 
 * A getCell is the smallest editable bit of world data in chunk stories. A
 * getCell contains a Voxel type, 8 bits of free-form metadata and 2x 4-bits of
 * voxel lightning ( for sun/skylight and voxel-emitted light ) */
public interface CellData {
	public World getWorld();

	public int getX();

	public int getY();

	public int getZ();

	public default Location getLocation() {
		// Just here so dummy classes are shorter, actual voxel contexts have a field
		// for this
		return new Location(getWorld(), getX(), getY(), getZ());
	}

	/** Return the Voxel type used in this getCell */
	@Nullable
	public Voxel getVoxel();

	public int getMetaData();

	public int getSunlight();

	public int getBlocklight();

	// Neightbor cells access

	public CellData getNeightbor(int side);

	@Nullable
	public default Voxel getNeightborVoxel(int side) {
		return getNeightbor(side).getVoxel(); // Optimisation hint: do not newEntity the neightbor object if you just want
												// to peek the voxel
	}

	public default int getNeightborMetadata(int side) {
		return getNeightbor(side).getMetaData(); // Optimisation hint: do not newEntity the neightbor object if you just
													// want to peek the metadata
	}

	// Shortcuts

	/* @Nullable public default VoxelRenderer getVoxelRenderer() { Voxel voxel =
	 * getVoxel(); return voxel != null ? voxel.getVoxelRenderer(this) : null; }
	 * 
	 * @Nullable public default VoxelTexture getTexture(VoxelSide side) { Voxel
	 * voxel = getVoxel(); return voxel != null ? voxel.getVoxelTexture(side, this)
	 * : null; } */

	/** Returns an array (possibly 0-sized) of collision boxes translated to the
	 * actual position of the voxel */
	@Nullable
	public default Box[] getTranslatedCollisionBoxes() {
		Voxel voxel = getVoxel();
		return voxel != null ? voxel.getTranslatedCollisionBoxes(this) : new Box[] {};
	}
}