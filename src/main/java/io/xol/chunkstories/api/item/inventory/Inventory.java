//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.util.IterableIterator;

import javax.annotation.Nullable;

public interface Inventory extends Iterable<ItemPile> {
	/** How many slots wide is this inventory */
	public int getWidth();

	/** How many slots high is this inventory */
	public int getHeight();

	/** Name displayed in GUI, among other things */
	public String getInventoryName();

	/**
	 * Does someone 'owns' this ? Returns NULL or something implementing
	 * InventoryHolder.
	 */
	@Nullable
	public InventoryHolder getHolder();

	/**
	 * Returns the ItemPile in that position. This functions considers the fact that
	 * some items are wider than others, thus checking different positions can
	 * return the same items.
	 */
	@Nullable
	public ItemPile getItemPileAt(int x, int y);

	/**
	 * Checks if a spot in the inventory is eligible for placement of an ItemPile.
	 * Takes into account the size of the items, as well as item stacking.
	 */
	public boolean canPlaceItemAt(int x, int y, ItemPile pile);

	/**
	 * Tries to place an item at that location, it returns the argument 'pile' if it
	 * can't place it.
	 */
	@Nullable
	public ItemPile placeItemPileAt(int x, int y, ItemPile pile);

	/**
	 * Tries to replace the pile in the inventory with another pile The failure
	 * condition is that replacing the pile would cause it to 'overlap' neightbours
	 * and to prevent that the game will not let you do so.
	 * 
	 * @return true if it succeeds, false else
	 */
	public boolean setItemPileAt(int x, int y, @Nullable ItemPile pile);

	/**
	 * Try to add a pile to this inventory.
	 * 
	 * @param pile
	 * @return Null if it succeeds or the input pile if it fails
	 */
	@Nullable
	public ItemPile addItemPile(ItemPile pile);

	/** Iterates over every ItemPile */
	public IterableIterator<ItemPile> iterator();

	/** Removes all ItemPiles in the inventory. */
	public void clear();

	/** Counts the amount of stuff this inventory contains. */
	public int size();

	/** Marks said slot as updated */
	public void refreshItemSlot(int x, int y);

	/** Used to secure access */
	public boolean isAccessibleTo(@Nullable Entity entity);

	public final Logger inventoriesLogger = LoggerFactory.getLogger("inventory");

}