//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asInt
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.util.kotlin.initOnce
import java.lang.reflect.Constructor

class ItemDefinition(val store: Content.ItemsDefinitions, name: String, properties: Json.Dict) : Definition(name, properties) {
    /** When added to the game content, either by being loaded explicitly or programatically, will be set to an integer
     * value. Attempting to manually override/set this identifier yourself will result in a house fire. */
    var assignedId: Int by initOnce()

    val clazz: Class<Item>
    private val constructor: Constructor<Item>

    val slotsWidth: Int
    val slotsHeight: Int
    val maxStackSize: Int

    init {
        clazz = this["class"].asString?.let {
            store.parent.modsManager.getClassByName(it)?.let {
                if (Item::class.java.isAssignableFrom(it))
                    it as Class<Item>
                else
                    throw Exception("The custom class has to extend the Item class !")
            }
        } ?: Item::class.java

        constructor = try {
            clazz.getConstructor(ItemDefinition::class.java)
        } catch (e: NoSuchMethodException) {
            throw Exception("Your custom class, $clazz, lacks the correct Item(ItemDefinition) constructor.")
        }

        slotsWidth = this["slotsWidth"].asInt ?: 1
        slotsHeight = this["slotsHeight"].asInt ?: 1
        maxStackSize = this["maxStackSize"].asInt ?: 64
    }

    fun <I : Item> newItem() = (constructor.newInstance(this)!! as I)!!

    override fun toString(): String {
        return "ItemDefinition($name, $properties)"
    }
}