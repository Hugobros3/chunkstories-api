//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.Declaration

/** Represents an Item declaration, can instantiate them */
interface ItemDeclaration<T: Item> : Declaration {
    /** Reference to the containing store */
    fun store(): Content.ItemsDefinitions

    /** The actual class used. If none is specified, this will be Item::class */
    val clazz: Class<T>

    /** How many slots does the item take in an inventory, width-wise. */
    val slotsWidth: Int

    /** How many slots does the item take in an inventory, height-wise */
    val slotsHeight: Int

    /** Defines the maximal 'amount' an ItemPile can have of this item.  */
    val maxStackSize: Int

    /** Initialization code that is applied to the Item at initialization (but after constructor)*/
    val prototype: T.() -> Unit

    /** Instructions for building a representation based on the item */
    val representation: ItemRepresentationBuilder<T>

    /** Instantiates the class 'clazz' and applies the prototype to it */
    fun newItem(): T
}