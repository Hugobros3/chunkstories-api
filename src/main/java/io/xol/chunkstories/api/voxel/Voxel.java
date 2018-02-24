//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.item.ItemVoxel;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.voxel.materials.Material;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.cell.FutureCell;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;
import io.xol.chunkstories.api.world.chunk.Chunk.FreshChunkCell;

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
	
	/** Returns true only if this voxel is the 'void' air type */
	public final boolean isAir() {
		return store().air().sameKind(this);
	}

	/** Returns the internal, non localized name of this voxel, shortcut to VoxelType */
	public final String getName() {
		return definition.getName();
	}
	
	/** Returns the Material used by this Voxel */
	public final Material getMaterial() {
		return definition.getMaterial();
	}

	/** @return The custom rendered used or null if default */
	public VoxelRenderer getVoxelRenderer(CellData info) {
		return voxelRenderer;
	}
	
	/**
	 * Called before setting a cell to this Voxel type. Previous state is assumed to be air.
	 * @param newData The data we want to place here. You are welcome to modify it !
	 * @throws Throw a IllegalBlockModificationException if you want to stop the modification from happening altogether.
	 */
	public void onPlace(FutureCell cell, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
	
	/** 
	 * Called <i>after</i> a cell was successfully placed.
	 * Unlike onPlace you can add your voxelComponents here.
	 * @param cell 
	 */
	public void whenPlaced(FreshChunkCell cell) {
		
	}
	
	/**
	 * Called before replacing a cell contaning this voxel type with air.
	 * @param context Current data in this cell.
	 * @param cause The cause of this modification ( can be an Entity )
	 * @throws Throw a IllegalBlockModificationException if you want to stop the modification from happening.
	 */
	public void onRemove(ChunkCell cell, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
	
	/**
	 * Called when either the metadata, block_light or sun_light values of a cell of this Voxel type is touched.
	 * @param context The current data in this cell.
	 * @param newData The future data we want to put there
	 * @param cause The cause of this modification ( can be an Entity )
	 * @throws IllegalBlockModificationException If we want to prevent it
	 */
	public void onModification(ChunkCell context, FutureCell newData, WorldModificationCause cause) throws WorldException {
		//Do nothing
	}
	
	/**
	 * Called when an Entity's controller sends an Input while looking at this Cell
	 * @return True if the interaction was 'handled', and won't be passed to the next stage of the input pipeline
	 */
	public boolean handleInteraction(Entity entity, ChunkCell voxelContext, Input input) {
		return false;
	}
	
	/**
	 * Gets the Blocklight level this voxel emits
	 * @return The aformentioned light level
	 */
	public byte getEmittedLightLevel(CellData info) {
		//By default the light output is the one defined in the type, you can change it depending on the provided data
		return definition.getEmittedLightLevel();
	}

	/**
	 * Gets the texture for this voxel
	 * @param side The side of the block we want the texture of ( see {@link VoxelSides VoxelSides.class} )
	 */
	public VoxelTexture getVoxelTexture(VoxelSides side, CellData info) {
		//By default we don't care about context, we give the same texture to everyone
		return definition.getVoxelTexture(side);
	}

	/**
	 * Gets the reduction of the light that will transfer from this block to another, based on data from the two blocks and the side from wich it's leaving the first block from.
	 * 
	 * @param in The full data related the entry cell (==this one) ( see {@link VoxelFormat VoxelFormat.class} )
	 * @param out The full data related the exit voxel ( see {@link VoxelFormat VoxelFormat.class} )
	 * @param side The side of the block light would come out of ( see {@link VoxelSides VoxelSides.class} )
	 * @return The reduction to apply to the light level on exit
	 */
	public int getLightLevelModifier(CellData in, CellData out, VoxelSides side) {
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
	public CollisionBox[] getTranslatedCollisionBoxes(CellData info) {
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
	public CollisionBox[] getCollisionBoxes(CellData info) {
		if (!definition.isSolid())
			return new CollisionBox[] {};
		return new CollisionBox[] { new CollisionBox( definition.getCollisionBox()) }; //Return the one box in the definition, if you want more make a customClass
	}

	/** Two voxels are of the same kind if they share the same definition. */
	public final boolean sameKind(Voxel voxel) {
		return this.getDefinition() == voxel.getDefinition();
	}

	/** @return Returns an array of ItemPiles for all the player-placeable variants of this Voxel */
	public ItemPile[] getItems() {
		//We spawn a ItemVoxel and set it to reflect this one
		ItemVoxel itemVoxel = (ItemVoxel) this.getDefinition().store().parent().items().getItemTypeByName("item_voxel").newItem();
		itemVoxel.voxel = this;
		return new ItemPile[] { new ItemPile(itemVoxel) };
	}
	
	public String toString() {
		return "[Voxel name:"+getName()+"]";
	}
	
	public final Content.Voxels store() {
		return store;
	}
}
