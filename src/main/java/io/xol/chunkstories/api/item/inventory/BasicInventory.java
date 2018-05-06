//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item.inventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import io.xol.chunkstories.api.content.ContentTranslator;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.exceptions.NullItemException;
import io.xol.chunkstories.api.exceptions.UndefinedItemTypeException;
import io.xol.chunkstories.api.item.Item;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;
import io.xol.chunkstories.api.util.IterableIterator;

import javax.annotation.Nullable;

/** Mostly the data structure that actually holds items */
public class BasicInventory implements Inventory {

	protected int width;
	protected int height;

	protected ItemPile[][] contents;
	
	protected Inventory externalInventory = this;

	public BasicInventory(int width, int height, Inventory externalInventory) {
		this(width, height);
		this.externalInventory = externalInventory;
	}
	
	public BasicInventory(int width, int height) {

		this.width = width;
		this.height = height;

		this.contents = new ItemPile[width][height];
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public ItemPile getItemPileAt(int x, int y) {
		if (contents[x % width][y % height] != null)
			return contents[x % width][y % height];
		else {
			ItemPile p;
			for (int i = 0; i < x + (1); i++) {
				// If overflow in width
				if (i >= width)
					break;
				for (int j = 0; j < y + (1); j++) {
					// If overflow in height
					if (j >= height)
						break;
					p = contents[i % width][j % height];
					if (p != null) {
						if (i + p.getItem().getDefinition().getSlotsWidth() - 1 >= x && j + p.getItem().getDefinition().getSlotsHeight() - 1 >= y)
							return p;
					}
				}
			}
			return null;
		}
	}

	@Override
	public boolean canPlaceItemAt(int x, int y, ItemPile itemPile) {
		if (contents[x % width][y % height] != null) {
			return false;
		} else {
			ItemPile p;
			// Iterate the inventory up to the new pile x end ( position + width - 1 )
			for (int i = 0; i < x + (itemPile.getItem().getDefinition().getSlotsWidth()); i++) {
				// If the item width would overflow the limits of the inventory
				if (i >= width)
					return false;
				for (int j = 0; j < y + (itemPile.getItem().getDefinition().getSlotsHeight()); j++) {
					// If overflow in height
					if (j >= height)
						return false;
					// Check nearby items don't overlap our pile
					p = contents[i % width][j % height];
					if (p != null) {
						if (i + p.getItem().getDefinition().getSlotsWidth() - 1 >= x && j + p.getItem().getDefinition().getSlotsHeight() - 1 >= y)
							return false;
					}
				}
			}
			return true;
		}
	}

	@Override
	public ItemPile placeItemPileAt(int x, int y, ItemPile itemPile) {
		ItemPile currentPileAtLocation = this.getItemPileAt(x, y);
		// If empty and has space, put it in.
		if (currentPileAtLocation == null && canPlaceItemAt(x, y, itemPile)) {
			itemPile.setInventory(externalInventory);

			itemPile.setX(x);
			itemPile.setY(y);
			contents[x % width][y % height] = itemPile;

			// Push changes
			this.refreshItemSlot(x, y, contents[x % width][y % height]);

			// There is nothing left
			return null;
		}
		// If the two piles are similar we can try to merge them
		if (currentPileAtLocation != null && currentPileAtLocation.canMergeWith(itemPile) && !currentPileAtLocation.equals(itemPile)) {
			Item item = currentPileAtLocation.getItem();
			int currentAmount = currentPileAtLocation.getAmount();
			int wouldBeAddedAmount = itemPile.getAmount();

			// The existing pile is not already full
			if (currentAmount < item.getDefinition().getMaxStackSize()) {
				int totalAmount = currentAmount + wouldBeAddedAmount;
				// How much can we add ?
				int addableAmmount = Math.min(totalAmount, item.getDefinition().getMaxStackSize()) - currentAmount;

				currentPileAtLocation.setAmount(currentAmount + addableAmmount);
				// If we could add all to the first stack, discard the second pile
				if (addableAmmount == wouldBeAddedAmount) {
					// Push changes
					this.refreshItemSlot(x, y, contents[x % width][y % height]);

					return null;
				}
				// If we couldn't, reduce it's size
				else {
					itemPile.setAmount(wouldBeAddedAmount - addableAmmount);

					// Push changes
					this.refreshItemSlot(x, y, contents[x % width][y % height]);

					return itemPile;
				}
			}
		}
		// If none of the above can be done, return the original pile.
		return itemPile;
	}

	@Override
	public boolean setItemPileAt(int x, int y, @Nullable ItemPile pile) {
		if (pile == null) {
			contents[x % width][y % height] = null;

			this.refreshItemSlot(x, y, contents[x % width][y % height]);

			return true;
		}
		ItemPile temp = null;
		if (contents[x % width][y % height] != null) {
			temp = contents[x % width][y % height];
			contents[x % width][y % height] = null;
		}

		if (canPlaceItemAt(x, y, pile)) {
			pile.setInventory(externalInventory);
			pile.setX(x);
			pile.setY(y);
			contents[x % width][y % height] = pile;
		} else {
			contents[x % width][y % height] = temp;
			this.refreshItemSlot(x, y, contents[x % width][y % height]);
			return false;
		}

		this.refreshItemSlot(x, y, contents[x % width][y % height]);
		return true;
	}

	@Override
	public ItemPile addItemPile(ItemPile pile) {
		for (int j = 0; j < height; j++)
			for (int i = 0; i < width; i++)
				if (placeItemPileAt(i, j, pile) == null)
					return null;
		return pile;
	}

	public IterableIterator<ItemPile> iterator() {
		return new BasicInventoryIterator();
	}

	public class BasicInventoryIterator implements IterableIterator<ItemPile> {
		public int i = 0;
		public int j = 0;

		ItemPile current = contents[0][0];

		@Override
		public boolean hasNext() {
			while (current == null && !reachedEnd()) {
				i++;
				if (i >= width) {
					i = 0;
					j++;
				}
				if (reachedEnd())
					return false;
				current = contents[i][j];
			}
			return current != null;
		}

		private boolean reachedEnd() {
			return j >= height;
		}

		@Override
		public ItemPile next() {
			if (reachedEnd())
				return null;

			if (current == null)
				hasNext();

			ItemPile r = current;
			current = null;
			return r;
		}

		@Override
		public void remove() {
			contents[i][j] = null;
		}
	}

	/** Updates the inventory for whoever is watching it */
	public void refreshCompleteInventory() {
		// Do nothing !
	}

	/** Updates the inventory slot for whoever is watching it */
	public void refreshItemSlot(int x, int y, @Nullable ItemPile pileChanged) {
		// Don't do shit either
	}

	/** Alias for refreshItemSlot(x, y, pileChanged); */
	public void refreshItemSlot(int x, int y) {
		refreshItemSlot(x, y, this.contents[x][y]);
	}

	/** Saves the inventory content to a data stream */
	public void pushInventory(StreamTarget destinator, DataOutputStream stream, ContentTranslator translator) throws IOException {
		stream.writeInt(width);
		stream.writeInt(height);

		ItemPile pile;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				pile = contents[i][j];
				if (pile == null)
					stream.writeInt(0);
				else {
					pile.saveIntoStream(translator, stream);
				}
			}
	}

	/**
	 * Loads the inventory content from a data stream
	 * 
	 * @param content
	 *            Requires a reference to a Content instance so it can instanciate
	 *            the items
	 * @throws IOException
	 */
	public void pullInventory(StreamSource from, DataInputStream stream, ContentTranslator translator) throws IOException {
		this.width = stream.readInt();
		this.height = stream.readInt();

		contents = new ItemPile[width][height];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				ItemPile itemPile;
				try {
					itemPile = ItemPile.obtainItemPileFromStream(translator, stream);
					// Then add the thing
					contents[i][j] = itemPile;
					contents[i][j].setInventory(externalInventory);
					contents[i][j].setX(i);
					contents[i][j].setY(j);
				} catch (NullItemException e) {
					// Don't do anything about it, no big deal
				} catch (UndefinedItemTypeException e) {
					// This is slightly more problematic
					inventoriesLogger.error("Undefined item: ", e);
				}
			}
	}

	@Override
	public void clear() {
		contents = new ItemPile[width][height];

		this.refreshCompleteInventory();
	}

	@Override
	public int size() {
		int size = 0;
		Iterator<ItemPile> i = this.iterator();
		while (i.hasNext()) {
			i.next();
			size++;
		}
		return size;
	}

	@Override
	public String getInventoryName() {
		return "Unamed Inventory";
	}

	@Nullable
	@Override
	public InventoryHolder getHolder() {
		return null;
	}

	@Override
	public boolean isAccessibleTo(@Nullable Entity entity) {
		return true;
	}
}
