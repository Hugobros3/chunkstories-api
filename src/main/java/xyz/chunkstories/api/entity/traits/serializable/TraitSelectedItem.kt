//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asInt
import xyz.chunkstories.api.content.json.stringSerialize
import xyz.chunkstories.api.content.json.toJson
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.OfflineSerializedData
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class TraitSelectedItem(entity: Entity, traitInventory: TraitInventory) : Trait(entity), TraitSerializable, TraitNetworked<TraitSelectedItem.SelectedItemUpdate> {
    override val serializedTraitName = "selectedItem"

    private var inventory: Inventory
    var selectedSlot = 0
    set(value) {
        field = value % inventory.width
        if (field < 0)
            field += inventory.width

        sendMessageAllSubscribersButController(SelectedItemUpdate.ServerToClientsUpdate(selectedItem?.item, selectedItem?.amount ?: 0))
    }

    val selectedItem: ItemPile?
        get() = inventory.getItemPileAt(selectedSlot, 0)

    init {
        this.inventory = traitInventory.inventory
    }

    sealed class SelectedItemUpdate : TraitMessage() {
        data class ServerToClientsUpdate(val item: Item?, val amount: Int) : SelectedItemUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.write(0)
                dos.writeUTF(serializeItemAndAmount(item, amount).stringSerialize())
            }
        }

        data class ControllerUpdate(val slot: Int) : SelectedItemUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.write(1)
                dos.writeInt(slot)
            }
        }
    }

    override fun readMessage(dis: DataInputStream): SelectedItemUpdate {
        val type = dis.read()
        return when(type) {
            0 -> deserializeItemAndAmount(entity.world.contentTranslator, dis.readUTF().toJson()).let {
                SelectedItemUpdate.ServerToClientsUpdate(it.first, it.second)
            }
            1 -> SelectedItemUpdate.ControllerUpdate(dis.readInt())
            else -> throw Exception()
        }
    }

    override fun processMessage(message: SelectedItemUpdate, from: Interlocutor) {
        when(message) {
            is SelectedItemUpdate.ServerToClientsUpdate -> {
                if(entity.world is WorldMaster)
                    return

                // other players don't get to know the full contents of another's inventory
                inventory.setItemAt(0, 0, message.item, message.amount, true)
            }
            is SelectedItemUpdate.ControllerUpdate -> {
                selectedSlot = message.slot
            }
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if(subscriber is Controller && subscriber == entity.traits[TraitControllable::class]?.controller) {
            sendMessage(subscriber, SelectedItemUpdate.ControllerUpdate(selectedSlot))
        } else {
            sendMessage(subscriber, SelectedItemUpdate.ServerToClientsUpdate(selectedItem?.item, selectedItem?.amount ?: 0))
        }
    }

    override fun tick() {
        // Tick item in hand if one such exists
        selectedItem?.let { it.item.tickInHand(entity, it) }
    }

    override fun serialize() = Json.Value.Number(selectedSlot.toDouble())

    override fun deserialize(json: Json) {
        selectedSlot = json.asInt ?: selectedSlot
    }
}
