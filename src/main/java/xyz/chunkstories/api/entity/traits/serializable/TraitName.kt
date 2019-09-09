//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asString
import java.io.DataInputStream
import java.io.DataOutputStream

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.world.WorldClient
import xyz.chunkstories.api.world.WorldMaster

class TraitName(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitName.NameUpdate> {
    override val serializedTraitName = "name"

    var name = ""
        set(value) {
            field = value

            if(entity.world is WorldMaster)
                sendMessageAllSubscribers(NameUpdate(value))
        }

    override fun deserialize(json: Json) {
        name = json.asString ?: name
    }

    override fun serialize(): Json {
        return Json.Value.Text(name)
    }

    override fun processMessage(message: NameUpdate, from: Interlocutor) {
        if(entity.world is WorldClient) {
            name = message.name
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        sendMessage(subscriber, NameUpdate(name))
    }

    override fun readMessage(dis: DataInputStream): NameUpdate {
        return NameUpdate(dis.readUTF())
    }

    data class NameUpdate(val name: String) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeUTF(name)
        }
    }
}