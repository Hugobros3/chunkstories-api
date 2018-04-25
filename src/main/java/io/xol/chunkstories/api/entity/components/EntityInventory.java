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
import io.xol.chunkstories.api.item.inventory.BasicInventory;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.item.inventory.InventoryHolder;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.net.packets.PacketInventoryPartialUpdate;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public class EntityInventory extends EntityComponent implements Inventory {
	protected BasicInventory actualInventory;
	public InventoryHolder holder;

	public EntityInventory(Entity entity, int width, int height) {
		super(entity);
		if (!(entity instanceof InventoryHolder))
			throw new RuntimeException("You can only add an inventory to an entity if it implements the InventoryHolder interface.");

		this.holder = (InventoryHolder) entity;
		
		//Create the REAL inventory but re-route the updaters method to this class so it's functional
		this.actualInventory = new BasicInventory(width, height) {
			public void refreshItemSlot(int x, int y, ItemPile pileChanged) {
				EntityInventory.this.refreshItemSlot(x, y, pileChanged);
			}

			@Override
			public void refreshCompleteInventory() {
				EntityInventory.this.refreshCompleteInventory();
			}
		};
	}

	@Override
	public InventoryHolder getHolder() {
		return holder;
	}
	
	@Override
	public String getInventoryName()
	{
		String name = entity.components.tryWith(EntityName.class, en -> en.getName());
		//if (holder instanceof EntityNameable)
		//	return ((EntityNameable) holder).getName();
		return name != null ? null : holder.getClass().getSimpleName();
	}

	@Override
	public void refreshItemSlot(int x, int y)
	{
		actualInventory.refreshItemSlot(x, y);
	}

	public void refreshItemSlot(int x, int y, ItemPile pileChanged)
	{
		Packet packetItemUpdate = new PacketInventoryPartialUpdate(entity.world, this, x, y, pileChanged);
		entity.components.with(EntityController.class, ecc -> { if(ecc.getController() != null) ecc.getController().pushPacket(packetItemUpdate); } );
		
		/*Controller controller = null;
		if (entity instanceof EntityControllable)
			controller = ((EntityControllable) entity).getControllerComponent().getController();

		if (controller != null)
			controller.pushPacket(packetItemUpdate);*/
	}
	
	public void refreshCompleteInventory()
	{
		pushComponentController();
	}
	
	public boolean isAccessibleTo(Entity entity) {
		
		if(entity == null)
			return true;
		
		//You always have access to yourself
		if(entity == EntityInventory.this.entity)
			return true;
		
		//Dead entities ain't got no rights
		//TODO this should be a more general ban on interaction from dead stuff
		
		//if(EntityInventory.this.entity instanceof EntityLiving && ((EntityLiving)EntityInventory.this.entity).isDead())
		//	return true;
		
		return false;
	}

	// Room for expansion
	public enum UpdateMode
	{
		TOTAL_REFRESH,
		NEVERMIND,
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
		actualInventory.pushInventory(destinator, stream, entity.world.getContentTranslator());
	}

	@Override
	protected void pull(StreamSource from, DataInputStream stream) throws IOException
	{
		//Unused
		byte b = stream.readByte();
		
		//Ignore NVM stuff
		if(b == UpdateMode.NEVERMIND.ordinal())
			return;

		actualInventory.pullInventory(from, stream, entity.world.getContentTranslator());
	}

	@Override
	public int getWidth() {
		return actualInventory.getWidth();
	}

	@Override
	public int getHeight() {
		return actualInventory.getHeight();
	}

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
	public boolean setItemPileAt(int x, int y, ItemPile pile) {
		return actualInventory.setItemPileAt(x, y, pile);
	}

	@Override
	public ItemPile addItemPile(ItemPile pile) {
		return actualInventory.addItemPile(pile);
	}

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
