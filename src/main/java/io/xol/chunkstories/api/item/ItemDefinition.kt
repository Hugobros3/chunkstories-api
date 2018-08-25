//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.Declaration
import io.xol.chunkstories.api.content.Definition
import io.xol.chunkstories.api.rendering.item.ItemRenderer

/** Represents an Item declaration, can instantiate them */
interface ItemDefinition<T: Item> : Declaration {
    /** The actual class used. If none is specified, this will be Item::class */
    val clazz: Class<T>

    /** How many slots does the item take in an inventory, width-wise. */
    val slotsWidth: Int

    /** How many slots does the item take in an inventory, height-wise */
    val slotsHeight: Int

    /** Defines the maximal 'amount' an ItemPile can have of this item.  */
    val maxStackSize: Int

    /** Initialization code that is applied to the Item at initialization */
    val prototype: T.() -> Unit

    /** Instructions for building a representation based on the item */
    val representation: ItemRepresentationBuildingContext<T>.() -> Unit

    /** Instantiates the class 'clazz' and applies the prototype to it */
    fun newItem(): T

    fun store(): Content.ItemsDefinitions
}