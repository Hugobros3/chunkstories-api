package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.voxel.materials.Material;
import io.xol.chunkstories.api.voxel.models.VoxelModel;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;

public interface VoxelDefinition extends Definition
{
	/** Get the store where the voxel definitions are stored. */
	public Content.Voxels store();
	
	///** Get the assignated ID for this voxel */
	//public int getId();

	/** Returns the internal, non localized name of this voxel */
	public String getName();
	
	/** Returns the material used by this Voxel */
	public Material getMaterial();

	/** Returns the voxelModel specified in the .voxels file, or null. */
	public VoxelModel getVoxelModel();

	/** Returns the collisionBox defined in the .voxels file, or a default one if none was. */
	public CollisionBox getCollisionBox();
	
	/** 
	 * Gets the texture for this voxel
	 * @param side The side of the block we want the texture of ( see {@link VoxelSides VoxelSides.class} ) 
	 */
	public VoxelTexture getVoxelTexture(VoxelSides side);

	public boolean isSolid();

	public boolean isOpaque();
	
	public byte getShadingLightLevel();

	public byte getEmittedLightLevel();

	public boolean isBillboard();

	public boolean isLiquid();

	public boolean isSelfOpaque();

	public boolean isSelectable();
}
