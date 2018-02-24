//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.item;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.CancellableEvent;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.events.entity.EntityDeathEvent;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.item.inventory.ItemPile;

public class EventItemDroppedToWorld extends CancellableEvent
{
	// Every event class has to have this
	
	static EventListeners listeners = new EventListeners(EntityDeathEvent.class);
	
	@Override
	public EventListeners getListeners()
	{
		return listeners;
	}
	
	public static EventListeners getListenersStatic()
	{
		return listeners;
	}
	
	// Specific event code
	
	private Location location;
	private Inventory inventoryFrom;
	private ItemPile itemPile;
	
	/** The entity to be added to the world that will represent this item pile */
	private Entity itemEntity;

	public EventItemDroppedToWorld(Location location, Inventory inventoryFrom, ItemPile itemPile) {
		this.location = location;
		this.inventoryFrom = inventoryFrom;
		this.itemPile = itemPile;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Inventory getInventoryFrom() {
		return inventoryFrom;
	}

	public void setInventoryFrom(Inventory inventoryFrom) {
		this.inventoryFrom = inventoryFrom;
	}

	public ItemPile getItemPile() {
		return itemPile;
	}

	public void setItemPile(ItemPile itemPile) {
		this.itemPile = itemPile;
	}
	
	public Entity getItemEntity() {
		return itemEntity;
	}

	public void setItemEntity(Entity itemEntity) {
		this.itemEntity = itemEntity;
	}
}
