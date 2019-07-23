//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item

import org.joml.Matrix4f
import org.joml.Vector4f
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.Sprite
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.inventory.ItemPile
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
    open fun onControllerInput(entity: Entity, pile: ItemPile, input: Input, controller: Controller): Boolean = false

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

        val data = ByteArrayOutputStream()
        try {
            this.save(DataOutputStream(data))

            val stream = ByteArrayInputStream(data.toByteArray())
            val dis = DataInputStream(stream)

            new.load(dis)
            dis.close()
        } catch (e: IOException) {
        }

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

    /** Unsafe, called upon loading this item from a stream. If you do use it,
     * PLEASE ensure you remember how many bytes you read/write and be consistent,
     * else you break the savefile  */
    @Throws(IOException::class)
    open fun load(stream: DataInputStream) {
    }

    /** See load().  */
    @Throws(IOException::class)
    open fun save(stream: DataOutputStream) {
    }
}