//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.rendering.item.ItemRenderer;

public class Item implements Cloneable {
	private final ItemDefinition definition;

	public Item(ItemDefinition type) {
		this.definition = type;
	}

	public ItemDefinition getDefinition() {
		return definition;
	}

	public String getName() {
		return definition.getInternalName();
	}

	/**
	 * Returns null by default, you can have custom Item renderers just by returning
	 * an Item renderer here.
	 */
	public ItemRenderer getCustomItemRenderer(ItemRenderer fallbackRenderer) {
		// return new MyFancyCustomRenderer(fallbackRenderer);
		return fallbackRenderer;
	}

	/**
	 * Should be called when the owner has this item selected
	 * 
	 * @param owner
	 */
	public void tickInHand(Entity owner, ItemPile itemPile) {

	}

	/**
	 * Handles some input from the user
	 * 
	 * @param user
	 * @param pile
	 * @param input
	 * @return false if the item doesn't handle the input, true if it does
	 */
	public boolean onControllerInput(Entity owner, ItemPile pile, Input input, Controller controller) {
		return false;
	}

	/**
	 * Use : determine if two stacks can be merged together, should be overriden
	 * when items have extra info.
	 * 
	 * @return Returns true if the two items are similar and can share a stack
	 *         without loosing information.
	 */
	public boolean canMergeWith(Item item) {
		return definition.equals(item.getDefinition());
	}

	/**
	 * For Items not implementing a custom renderer, it just shows a dull icon and
	 * thus require an icon texture.
	 * 
	 * @return The full path to the image file.
	 */
	public String getTextureName(ItemPile pile) {
		return "./items/icons/" + getInternalName() + ".png";
	}

	/** Returns the assignated ID for this item. */
	/*
	 * public final int getID() { return type.getID(); }
	 */

	public String getInternalName() {
		return definition.getInternalName();
	}

	/**
	 * Unsafe, called upon loading this item from a stream. If you do use it, PLEASE
	 * ensure you remember how many bytes you read/write and be consistent, else you
	 * break the savefile
	 */
	public void load(DataInputStream stream) throws IOException {
	}

	/** See load(). */
	public void save(DataOutputStream stream) throws IOException {
	}
}
