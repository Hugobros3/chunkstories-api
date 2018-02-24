//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.item;

import org.joml.Matrix4f;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.world.World;

/**
 * Instanced once per ItemDefinition, has the job of rendering all the Item instances from that definition.
 */
public class ItemRenderer
{
	protected final ItemRenderer fallbackRenderer;
	
	public ItemRenderer(ItemRenderer fallbackRenderer) {
		this.fallbackRenderer = fallbackRenderer;
	}

	/**
	 * Renders the item for the 2D inventory overlay
	 * @param context
	 * @param pile
	 * @param f
	 * @param g
	 * @param scaling
	 */
	public void renderItemInInventory(RenderingInterface renderingInterface, ItemPile pile, float f, float g, int scaling) {
		fallbackRenderer.renderItemInInventory(renderingInterface, pile, f, g, scaling);
	}

	/**
	 * Renders the item in the hand of the playing entity (or wherever the entity model is shown holding items)
	 * @param renderingContext
	 * @param pile
	 * @param handTransformation Can be modified
	 */
	public void renderItemInWorld(RenderingInterface renderingInterface, ItemPile pile, World world, Location location, Matrix4f transformation) {
		fallbackRenderer.renderItemInWorld(renderingInterface, pile, world, location, transformation);
	}
	
	/** Cleanups any used ressources */
	public void destroy() {
		
	}
}
