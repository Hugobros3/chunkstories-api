//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui

import io.xol.chunkstories.api.client.Client
import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.input.Mouse
import io.xol.chunkstories.api.item.inventory.Inventory

/** An abstraction for the GUI layer. */
interface Gui {
    val client: Client

    /** The size of the GUI viewport. May not actually be the size of the game window because of GUI scaling */
    val viewportWidth: Int

    /** The size of the GUI viewport. May not actually be the size of the game window because of GUI scaling */
    val viewportHeight: Int

    /** The top layer of the GUI, if one exists. (Otherwise just the game output and maybe debug information is shown)*/
    var topLayer: Layer?

    /** Pops the top layer : Assuming there is already a top layer, puts it's parent layer at the top */
    fun popTopLayer() {
        topLayer?.let {
            topLayer = it.parentLayer
        }
    }

    val mouse: Mouse
    fun hasFocus() : Boolean

    /** Font manager */
    val fonts: Fonts

    fun localization(): Content.Translation

    /** Opens the inventory GUI with all the specified inventories opened  */
    fun openInventories(vararg inventories: Inventory)
}