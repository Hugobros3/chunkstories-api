//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.exceptions.NullItemException;
import io.xol.chunkstories.api.exceptions.UndefinedItemTypeException;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.serialization.OfflineSerializedData;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public class TraitSelectedItem extends TraitSerializable {
	Inventory inventory;

	public TraitSelectedItem(Entity entity, TraitInventory inventory) {
		super(entity);
		this.inventory = inventory;
	}

	int selectedSlot = 0;

	/** Selects the slot given
	 * 
	 * @param newSlot */
	public void setSelectedSlot(int newSlot) {
		while (newSlot < 0)
			newSlot += inventory.getWidth();
		selectedSlot = newSlot % inventory.getWidth();

		// TODO permissions check
		this.pushComponentEveryone();
	}

	/** Returns the selected slot
	 * 
	 * @return */
	public int getSelectedSlot() {
		return selectedSlot;
	}

	/** Returns the selected item
	 * 
	 * @return */
	public ItemPile getSelectedItem() {
		return inventory.getItemPileAt(selectedSlot, 0);
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeInt(selectedSlot);

		ItemPile pile = inventory.getItemPileAt(selectedSlot, 0);
		// System.out.println("Sending slot"+pile);
		// don't bother writing the item pile if we're not master or if we'd be telling
		// the controller about his own item pile
		if (pile == null || !(entity.getWorld() instanceof WorldMaster)
				|| entity.traits.tryWithBoolean(TraitControllable.class, ec -> ec.getController() == destinator))
			dos.writeBoolean(false);
		else {
			dos.writeBoolean(true);
			pile.saveIntoStream(entity.getWorld().getContentTranslator(), dos);
		}
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		selectedSlot = dis.readInt();

		boolean itemIncluded = dis.readBoolean();
		if (itemIncluded) {
			// System.out.println("reading item from packet for entity"+entity);
			// ItemPile pile;

			ItemPile itemPile = null;
			try {
				itemPile = ItemPile.obtainItemPileFromStream(entity.getWorld().getContentTranslator(), dis);
			} catch (NullItemException e) {
				// Don't do anything about it, no big deal

			} catch (UndefinedItemTypeException e) {
				// This is slightly more problematic
				this.entity.getWorld().getGameContext().logger().info(e.getMessage());
			}

			// Ensures only client worlds accepts such pushes
			if (!(entity.getWorld() instanceof WorldMaster))
				inventory.setItemPileAt(selectedSlot, 0, itemPile);
		}

		this.pushComponentEveryoneButController();
	}

	public void pushComponentInStream(StreamTarget to, DataOutputStream dos) throws IOException {
		if (to instanceof OfflineSerializedData)
			System.out.println("Not writing component SelectedItem to offline data");
		else
			super.pushComponentInStream(to, dos);
	}
}
