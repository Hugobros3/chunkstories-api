//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item.interfaces;

import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.rendering.RenderingInterface;

/**
 * An interface for items that draw on top of the 2d screen of the user ( but before actual GUI elements are)
 */
public interface ItemOverlay
{
	public void drawItemOverlay(RenderingInterface renderingInterface, ItemPile itemPile);
}
