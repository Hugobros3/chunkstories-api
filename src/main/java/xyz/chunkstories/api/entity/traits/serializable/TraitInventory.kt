//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.content.json.stringSerialize
import xyz.chunkstories.api.content.json.toJson
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

open class TraitInventory(entity: Entity, width: Int, height: Int, val publicContents: Boolean = false) : Trait(entity), TraitSerializable, TraitNetworked<TraitInventory.InventoryUpdate>, InventoryCallbacks {
    override val serializedTraitName = "inventory"

    val inventory: Inventory //private set

    init {
        if (entity !is InventoryOwner)
            throw RuntimeException("You can only add an inventory to an entity if it implements the InventoryHolder interface.")

        inventory = Inventory(width, height, entity, this)
    }

    sealed class InventoryUpdate(val contentTranslator: ContentTranslator) : TraitMessage() {
        class RefreshSlot(val x: Int, val y: Int, val item: Item?, val amount: Int, contentTranslator: ContentTranslator) : InventoryUpdate(contentTranslator) {
            override fun write(dos: DataOutputStream) {
                dos.write(0)
                dos.writeInt(x)
                dos.writeInt(y)
                if (item == null) {
                    dos.writeInt(0)
                } else {
                    dos.writeInt(contentTranslator.getIdForItem(item))
                    dos.writeInt(amount)
                    dos.writeUTF(item.serialize().stringSerialize())
                }
            }
        }

        data class ItemInInventory(val x: Int, val y: Int, val item: Item, val amount: Int)
        class RefreshInventory(val contents: List<ItemInInventory>, contentTranslator: ContentTranslator) : InventoryUpdate(contentTranslator) {
            override fun write(dos: DataOutputStream) {
                dos.write(1)
                dos.writeInt(contents.size)
                for((x, y, item, amount) in contents) {
                    dos.writeInt(contentTranslator.getIdForItem(item))
                    dos.writeInt(amount)
                    dos.writeUTF(item.serialize().stringSerialize())
                }
            }
        }
    }

    override fun readMessage(dis: DataInputStream): InventoryUpdate {
        val contentTranslator = entity.world.contentTranslator
        val type = dis.read()
        return when (type) {
            0 -> {
                val x = dis.readInt()
                val y = dis.readInt()

                val itemId = dis.readInt()
                var amount = 0
                val item =
                if(itemId != 0) {
                    amount = dis.readInt()
                    val item = contentTranslator.getItemForId(itemId)!!.newItem<Item>()
                    item.deserialize(dis.readUTF().toJson().asDict!!)
                    item
                } else null

                InventoryUpdate.RefreshSlot(x, y, item, amount, contentTranslator)
            }
            1 -> {
                val count = dis.readInt()
                val list = mutableListOf<InventoryUpdate.ItemInInventory>()

                for(i in 0 until count) {
                    val x = dis.readInt()
                    val y = dis.readInt()
                    val itemId = dis.readInt()
                    val amount = dis.readInt()
                    val item = contentTranslator.getItemForId(itemId)!!.newItem<Item>()
                    item.deserialize(dis.readUTF().toJson().asDict!!)
                    list.add(InventoryUpdate.ItemInInventory(x, y, item, amount))
                }

                InventoryUpdate.RefreshInventory(list, contentTranslator)
            }
            else -> throw Exception()
        }
    }

    override fun processMessage(message: InventoryUpdate, from: Interlocutor) {
        if(entity.world is WorldMaster)
            return

        when(message) {
            is InventoryUpdate.RefreshSlot -> {
                inventory.setItemAt(message.x, message.y, message.item, message.amount)
            }
            is InventoryUpdate.RefreshInventory -> {
                inventory.clear()
                for((x, y, item, amount) in message.contents) {
                    inventory.setItemAt(x, y, item, amount)
                }
            }
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if(subscriber == entity.traits[TraitControllable::class]?.controller)
            sendMessageController(InventoryUpdate.RefreshInventory(inventory.contents.map { InventoryUpdate.ItemInInventory(it.x, it.y, it.item, it.amount) }, entity.world.contentTranslator))
    }

    override fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?) {
        sendMessageController(InventoryUpdate.RefreshSlot(x, y, pileChanged?.item, pileChanged?.amount ?: 0, entity.world.contentTranslator))
    }

    override fun refreshCompleteInventory() {
        sendMessageController(InventoryUpdate.RefreshInventory(inventory.contents.map { InventoryUpdate.ItemInInventory(it.x, it.y, it.item, it.amount) }, entity.world.contentTranslator))
    }

    override fun isAccessibleTo(entity: Entity): Boolean {
        return entity === this@TraitInventory.entity
    }

    override val inventoryName: String
        get() = entity.traits[TraitName::class]?.name ?: entity::class.java.simpleName

    override fun serialize() = InventorySerialization.serializeInventory(inventory, entity.world.contentTranslator)

    override fun deserialize(json: Json) {
        val dict = json.asDict ?: return
        InventorySerialization.deserializeInventory(inventory, entity.world.contentTranslator, dict)
    }
}
