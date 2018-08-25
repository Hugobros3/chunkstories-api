package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.content.DeclarationContext
import io.xol.chunkstories.api.item.Item
import kotlin.reflect.KClass

interface ItemDeclarationsContext {
    fun item(declaration: ItemDeclarationContext<Item>.() -> Unit)

    fun <T: Item> item(clazz: Class<T>, declaration: ItemDeclarationContext<T>.() -> Unit)
    fun <T: Item> item(clazz: KClass<T>, declaration: ItemDeclarationContext<T>.() -> Unit)

    var Item.name : String
}

interface ItemDeclarationContext<T: Item> : DeclarationContext{
    var slotsWidth: Int
    var slotsHeight: Int

    var maxStackSize: Int

    fun prototype(wow: T.() -> Unit)

    fun representation(representation: ItemRepresentationBuildingContext<T>.() -> Unit)
}

interface ItemRepresentationBuildingContext<T : Item> : DynamicRepresentationBuildingContext {
    val item: T
}