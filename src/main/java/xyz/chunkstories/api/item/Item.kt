//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item

import org.joml.Matrix4f
import org.joml.Vector4f
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.Sprite
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.util.kotlin.toVec4d
import java.io.*

open class Item(val definition: ItemDefinition) {
    open val name: String
        get() = definition.name

    /**
     *  Called every tick when an entity has this item in it's hand
     */
    open fun tickInHand(entity: Entity, itemPile: ItemPile) = Unit

    /** Called when an entity wielding this item is controlled and it's controller sends an input
     *
     * @return false if the item doesn't handle the input, true if it does
     */
    open fun onPlayerInput(entity: Entity, itemPile: ItemPile, input: Input, player: Player): Boolean = false

    /**
     * Use : determine if two stacks can be merged together, should be overriden
     * when items have extra info.
     *
     * @return true if the two items are similar and can share a stack without loosing information.
     */
    open fun canStackWith(item: Item): Boolean {
        return definition == item.definition
    }

    /** Deep-copies the item (via serialization & deserialization) */
    open fun duplicate(): Item {
        val new: Item = definition.newItem()

        new.deserialize(this.serialize())

        return new
    }

    /** For Items not implementing a custom renderer, it just shows a dull icon and
     * thus require an icon texture.
     *
     * @return The full path to the image file.
     */
    open fun getTextureName(): String {
        return "items/icons/${definition.name}.png"
    }

    open fun buildRepresentation(worldPosition: Matrix4f, representationsGobbler: RepresentationsGobbler) {
        representationsGobbler.acceptRepresentation(Sprite(worldPosition.transform(Vector4f(0.0f, 0f, 0f, 1f)).toVec4d().toVec3d(), 1f, MeshMaterial("item_$name", mapOf("albedoTexture" to getTextureName()), "opaque")))
    }

    @Throws(IOException::class)
    open fun serialize() : Json.Dict = Json.Dict(emptyMap())

    @Throws(IOException::class)
    open fun deserialize(json: Json.Dict) {}
}