//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.rendering.item.ItemRenderer;

/**
 * Immutable, describes an item type and is a common reference in all items of that type
 * It gets loaded from the .items file
 */
public interface ItemDefinition extends Definition
{
	///** @return Returns the associated ID in the .items files */
	//public int getID();
	
	/** @return The name this item is declared by */
	public String getInternalName();
	
	public Content.ItemsDefinitions store();

	/** Items in chunk stories can take up more than one slot.
	 * @return How many slots this items use, horizontally */
	public int getSlotsWidth();

	/** Items in chunk stories can take up more than one slot.
	 * @return How many slots this items use, vertically */
	public int getSlotsHeight();

	/** Defines the maximal 'amount' an ItemPile can have of this item. */
	public int getMaxStackSize();

	/** Instanciates a new item */
	public Item newItem();

	/** Returns a suitable ItemRenderer for this ItemType. Will return null if called on anything else than a Client. */
	public ItemRenderer getRenderer();
}