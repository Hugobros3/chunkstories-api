package io.xol.chunkstories.api.voxel;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.item.ItemVoxel;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.voxel.materials.Material;
import io.xol.chunkstories.api.voxel.models.VoxelRenderer;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Defines the behavior for associated with a voxel type declaration */
public class Voxel
{
	final protected VoxelDefinition definition;
	final protected Content.Voxels store;
	
	protected VoxelRenderer voxelRenderer;
	
	public Voxel(VoxelDefinition definition)
	{
		this.definition = definition;
		this.store = definition.store();

		//By default the 'VoxelRenderer' is just wether or not we set-up a model in the .voxels definitions file
		if(definition.getVoxelModel() != null)
			this.voxelRenderer = definition.getVoxelModel();
		//No custom model defined ? Use the default renderer.
		else
			this.voxelRenderer = definition.store().getDefaultVoxelRenderer();
	}
	
	/** Contains the information parsed from the .voxels file */
	public final VoxelDefinition getDefinition() {
		return definition;
	}
	
	/** Get the assignated ID for this voxel, shortcut to VoxelType */
	/*public final int getId() {
		return type.getId();
	}*/
	
	public final boolean isAir() {
		return store().air().sameKind(this);
	}

	/** Returns the internal, non localized name of this voxel, shortcut to VoxelType */
	public final String getName() {
		return definition.getName();
	}
	
	public final Material getMaterial() {
		return definition.getMaterial();
	}

	/** @return The custom rendered used or null if default */
	public VoxelRenderer getVoxelRenderer(VoxelContext info) {
		return voxelRenderer;
	}
	
	/** Can this Voxel be selected in creative mode ? (or is it skipped ?) */
	public boolean isVoxelSelectable() {
		//Air is intangible and so is water
		return definition.isSelectable();// getId() > 0 && !definition.isLiquid();
	}
	
	/**
	 * Gets the Blocklight level this voxel emmits
	 * @return The aformentioned light level
	 */
	public byte getLightLevel(VoxelContext info) {
		//By default the light output is the one defined in the type, you can change it depending on the provided data
		return definition.getEmittingLightLevel();
	}

	/**
	 * Gets the texture for this voxel
	 * @param side The side of the block we want the texture of ( see {@link VoxelSides VoxelSides.class} )
	 * @return
	 */
	public VoxelTexture getVoxelTexture(VoxelSides side, VoxelContext info) {
		//By default we don't care about context, we give the same texture to everyone
		return definition.getVoxelTexture(side);
	}

	/**
	 * Gets the reduction of the light that will transfer from this block to another, based on data from the two blocks and the side from wich it's leaving the first block from.
	 * 
	 * @param dataFrom The full 4-byte data related to this voxel ( see {@link VoxelFormat VoxelFormat.class} )
	 * @param dataTo The full 4-byte data related to this voxel ( see {@link VoxelFormat VoxelFormat.class} )
	 * @param side The side of the block light would come out of ( see {@link VoxelSides VoxelSides.class} )
	 * @return The reduction to apply to the light level on exit
	 */
	public int getLightLevelModifier(VoxelContext in, VoxelContext out, VoxelSides side) {
		if (getDefinition().isOpaque()) //Opaque voxels destroy all light
			return -15;
		return definition.getShadingLightLevel(); //Etc
	}

	/**
	 * Used to fine-tune the culling system, allows for a precise, per-face approach to culling.
	 * @param face The side of the block BEING DREW ( not the one we are asking ), so in fact we have to answer for the opposite face, that is the one that this voxel connects with. To get a reference on the sides conventions, see {@link VoxelSides VoxelSides.class}
	 * @param metadata The 8 bits of metadata associated with the block we represent.
	 * @return Whether or not that face occlude a whole face and thus we can discard it
	 */
	public boolean isFaceOpaque(VoxelSides side, int metadata) {
		return definition.isOpaque();
	}

	/**
	 * Get the collision boxes for this object, centered as if the block was in x,y,z
	 * 
	 * @param data The full 4-byte data related to this voxel ( see {@link VoxelFormat VoxelFormat.class} )
	 * @return An array of CollisionBox or null.
	 */
	public CollisionBox[] getTranslatedCollisionBoxes(VoxelContext info) {
		CollisionBox[] boxes = getCollisionBoxes(info);
		if (boxes != null)
			for (CollisionBox b : boxes)
				b.translate(info.getX(), info.getY(), info.getZ());
		return boxes;
	}
	
	/**
	 * Get the collision boxes for this object, centered as if the block was in 0,0,0
	 * 
	 * @param The
	 *            full 4-byte data related to this voxel ( see {@link VoxelFormat VoxelFormat.class} )
	 * @return An array of CollisionBox or null.
	 */
	public CollisionBox[] getCollisionBoxes(VoxelContext info) {
		if (!definition.isSolid())
			return new CollisionBox[] {};
		return new CollisionBox[] { new CollisionBox( definition.getCollisionBox()) }; //Return the one box in the definition, if you want more make a customClass
	}

	/** Two voxels are of the same kind if they share the same definition. */
	public boolean sameKind(Voxel voxel) {
		return this.getDefinition() == voxel.getDefinition();
	}

	/** @return Returns an array of ItemPiles to use in creative inventory */
	public ItemPile[] getItems() {
		//We spawn a ItemVoxel and set it to reflect this one
		ItemVoxel itemVoxel = (ItemVoxel) this.getDefinition().store().parent().items().getItemTypeByName("item_voxel").newItem();
		itemVoxel.voxel = this;
		return new ItemPile[] { new ItemPile(itemVoxel) };
	}
	
	public Content.Voxels store()
	{
		return store;
	}
}
