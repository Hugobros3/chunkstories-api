//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.interfaces

import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.item.inventory.ItemPile

/** An interface for items that draw on top of the 2d screen of the user ( but
 * before actual GUI elements are)  */
interface ItemOverlay {
    fun drawItemOverlay(drawer: GuiDrawer, itemPile: ItemPile)
}
