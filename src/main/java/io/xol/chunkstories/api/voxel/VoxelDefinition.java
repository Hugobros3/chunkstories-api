//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.voxel.materials.VoxelMaterial;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;

import javax.annotation.Nullable;

public interface VoxelDefinition extends Definition {
	/** Get the store where the voxel definitions are stored. */
	public Content.Voxels store();

	/** Returns the internal, non localized name of this voxel */
	public String getName();

	/** Returns the material used by this Voxel */
	public VoxelMaterial getMaterial();

	/** Returns the voxelModel specified in the .voxels file, or null. */
	@Nullable
	public VoxelRenderer getVoxelModel();

	/** Returns the collisionBox defined in the .voxels file, or a default one if
	 * none was. */
	public CollisionBox getCollisionBox();

	/** Gets the texture for this voxel
	 * 
	 * @param side The side of the block we want the texture of ( see
	 *            {@link VoxelSide VoxelSides.class} ) */
	public VoxelTexture getVoxelTexture(VoxelSide side);

	public boolean isSolid();

	public boolean isOpaque();

	public byte getShadingLightLevel();

	public byte getEmittedLightLevel();

	public boolean isBillboard();

	public boolean isLiquid();

	public boolean isSelfOpaque();

	public boolean isSelectable();
}
