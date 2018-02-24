//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.item.inventory.ItemPile;

public interface EntityWithSelectedItem extends EntityWithInventory
{
	public ItemPile getSelectedItem();
	
	public int getSelectedItemIndex();
	
	public void setSelectedItemIndex(int i);
	//public EntityComponentSelectedItem getSelectedItemComponent();
}
