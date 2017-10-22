package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.item.inventory.ItemPile;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface EntityWithSelectedItem extends EntityWithInventory
{
	public ItemPile getSelectedItem();
	
	public int getSelectedItemIndex();
	
	public void setSelectedItemIndex(int i);
	//public EntityComponentSelectedItem getSelectedItemComponent();
}
