//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.item.inventory.BasicInventory;
import xyz.chunkstories.api.item.inventory.Inventory;
import xyz.chunkstories.api.item.inventory.InventoryHolder;
import xyz.chunkstories.api.item.inventory.ItemPile;
import xyz.chunkstories.api.net.Packet;
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.util.IterableIterator;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TraitInventory extends TraitSerializable implements Inventory {
	protected BasicInventory actualInventory;
	public InventoryHolder holder;

	public TraitInventory(Entity entity, int width, int height) {
		super(entity);
		if (!(entity instanceof InventoryHolder))
			throw new RuntimeException("You can only add an inventory to an entity if it implements the InventoryHolder interface.");

		this.holder = (InventoryHolder) entity;

		// Create the REAL inventory but re-route the updaters method to this class so
		// it's functional
		this.actualInventory = new BasicInventory(width, height, this) {

			public void refreshItemSlot(int x, int y, @Nullable ItemPile pileChanged) {
				TraitInventory.this.refreshItemSlot(x, y, pileChanged);
			}

			@Override
			public void refreshCompleteInventory() {
				TraitInventory.this.refreshCompleteInventory();
			}
		};

	}

	@Override
	public InventoryHolder getHolder() {
		return holder;
	}

	@Override
	public String getInventoryName() {
		String name = getEntity().traits.tryWith(TraitName.class, TraitName::getName);
		// if (holder instanceof EntityNameable)
		// return ((EntityNameable) holder).getRegisteredComponentName();
		return name != null ? "[entity has no name]" : holder.getClass().getSimpleName();
	}

	@Override
	public void refreshItemSlot(int x, int y) {
		actualInventory.refreshItemSlot(x, y);
	}

	public void refreshItemSlot(int x, int y, @Nullable ItemPile pileChanged) {
		Packet packetItemUpdate = new PacketInventoryPartialUpdate(getEntity().getWorld(), this, x, y, pileChanged);
		getEntity().traits.with(TraitControllable.class, ecc -> {
			if (ecc.getController() != null)
				ecc.getController().pushPacket(packetItemUpdate);
		});
	}

	public void refreshCompleteInventory() {
		pushComponentController();
	}

	public boolean isAccessibleTo(@Nullable Entity entity) {

		if (entity == null)
			return true;

		// You always have access to yourself
		if (entity == TraitInventory.this.getEntity())
			return true;

		// Dead entities ain't got no rights
		// TODO this should be a more general ban on interaction from dead stuff

		// if(EntityInventory.this.entity instanceof EntityLiving &&
		// ((EntityLiving)EntityInventory.this.entity).isDead())
		// return true;

		return false;
	}

	// Room for expansion
	public enum UpdateMode
	{
		TOTAL_REFRESH, NEVERMIND,
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream stream) throws IOException {
		// Check that person has permission
		if (destinator instanceof Player) {
			Player player = (Player) destinator;
			Entity entity = player.getControlledEntity();

			// Abort if the entity don't have access
			if (!this.actualInventory.isAccessibleTo(entity)) {
				// System.out.println(player + "'s " + entity + " don't have access to "+this);
				stream.writeByte(UpdateMode.NEVERMIND.ordinal());
				return;
			}
		}
		stream.writeByte(UpdateMode.TOTAL_REFRESH.ordinal());
		actualInventory.pushInventory(destinator, stream, getEntity().getWorld().getContentTranslator());
	}

	@Override
	protected void pull(StreamSource from, DataInputStream stream) throws IOException {
		// Unused
		byte b = stream.readByte();

		// Ignore NVM stuff
		if (b == UpdateMode.NEVERMIND.ordinal())
			return;

		actualInventory.pullInventory(from, stream, getEntity().getWorld().getContentTranslator());
	}

	@Override
	public int getWidth() {
		return actualInventory.getWidth();
	}

	@Override
	public int getHeight() {
		return actualInventory.getHeight();
	}

	@Nullable
	@Override
	public ItemPile getItemPileAt(int x, int y) {
		return actualInventory.getItemPileAt(x, y);
	}

	@Override
	public boolean canPlaceItemAt(int x, int y, ItemPile pile) {
		return actualInventory.canPlaceItemAt(x, y, pile);
	}

	@Override
	public ItemPile placeItemPileAt(int x, int y, ItemPile pile) {
		return actualInventory.placeItemPileAt(x, y, pile);
	}

	@Override
	public boolean setItemPileAt(int x, int y, @Nullable ItemPile pile) {
		return actualInventory.setItemPileAt(x, y, pile);
	}

	@Override
	public ItemPile addItemPile(ItemPile pile) {
		return actualInventory.addItemPile(pile);
	}

	@NotNull
	@Override
	public IterableIterator<ItemPile> iterator() {
		return actualInventory.iterator();
	}

	@Override
	public void clear() {
		actualInventory.clear();
	}

	@Override
	public int size() {
		return actualInventory.size();
	}
}
