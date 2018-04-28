//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

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

public class EntitySelectedItem extends EntityComponent {
	Inventory inventory;

	public EntitySelectedItem(Entity entity, EntityInventory inventory) {
		super(entity);
		this.inventory = inventory;
	}

	int selectedSlot = 0;

	/**
	 * Selects the slot given
	 * 
	 * @param newSlot
	 */
	public void setSelectedSlot(int newSlot) {
		while (newSlot < 0)
			newSlot += inventory.getWidth();
		selectedSlot = newSlot % inventory.getWidth();

		// TODO permissions check
		this.pushComponentEveryone();

		/*
		 * if(this.holder != null && this.holder instanceof Entity && this.holder
		 * instanceof EntityControllable &&
		 * ((EntityControllable)this.holder).getController() != null &&
		 * ((EntityControllable)this.holder).getController() instanceof
		 * ClientController) { PacketItemUsage packet = new PacketItemUsage(true);
		 * packet.usage = ItemUsage.SELECT; packet.complementInfo = (byte) newSlot;
		 * if(((Entity) this.holder).getWorld() instanceof WorldRemoteClient)
		 * Client.connection.sendPacket(packet);
		 * //((ClientController)((EntityControllable)this.holder).getController()).
		 * notifySelectedItemChange(); }
		 */
	}

	/**
	 * Returns the selected slot
	 * 
	 * @return
	 */
	public int getSelectedSlot() {
		return selectedSlot;
	}

	/**
	 * Returns the selected item
	 * 
	 * @return
	 */
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
		if (pile == null || !(entity.getWorld() instanceof WorldMaster) || entity.components.tryWithBoolean(EntityController.class, ec -> ec.getController() == destinator))
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
				// Logger logger = this.entity.getWorld().getGameContext().logger();
				this.entity.getWorld().getGameContext().logger().info(e.getMessage());
				// e.printStackTrace(logger.getPrintWriter());
			}

			// Ensures only client worlds accepts such pushes
			if (!(entity.getWorld() instanceof WorldMaster))
				inventory.setItemPileAt(selectedSlot, 0, itemPile);

			/*
			 * int id = dis.readInt() & 0x00FFFFFF; ItemDefinition itemType =
			 * ItemDefinitions.getItemDefinitionById(id); if(itemType != null) { Item item =
			 * itemType.newItem(); pile = new ItemPile(item, dis); if(pile != null &&
			 * !(entity.getWorld() instanceof WorldMaster)) {
			 * //System.out.println("got held item for "+entity + " : "+pile);
			 * inventory.setItemPileAt(selectedSlot, 0, pile); } }
			 */
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
