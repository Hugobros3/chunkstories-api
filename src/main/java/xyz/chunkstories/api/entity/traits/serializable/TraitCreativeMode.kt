//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asBoolean
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.voxel.MiningTool
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

class TraitCreativeMode(entity: Entity) : TraitSerializable(entity), TraitNetworked<TraitCreativeMode.CreativeModeUpdate> {
    var enabled: Boolean = false
        set(value) {
            field = value
            // Only the controller cares about his creative status
            sendMessageController(CreativeModeUpdate(value))
        }

    companion object {
        val CREATIVE_MODE: WorldModificationCause = object : WorldModificationCause {

            override val name: String
                get() = "Creative Mode"
        }

        val CREATIVE_MODE_MINING_TOOL: MiningTool = object : MiningTool {
            override val miningEfficiency: Float = Float.POSITIVE_INFINITY
            override val toolTypeName: String = "hand"
        }
    }

    data class CreativeModeUpdate(val state: Boolean) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeBoolean(state)
        }
    }

    override fun readMessage(dis: DataInputStream) = CreativeModeUpdate(dis.readBoolean())

    override fun processMessage(message: CreativeModeUpdate, from: Interlocutor) {
        if(entity.world is WorldMaster) {
            return // players should use commands to toggle that rather
        }

        enabled = message.state
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        // Inform the controller of his status
        if(subscriber == entity.traits[TraitControllable::class]?.controller)
            sendMessage(subscriber, CreativeModeUpdate(enabled))
    }

    override fun serialize() = Json.Value.Bool(enabled)

    override fun deserialize(json: Json) {
        enabled = json.asBoolean ?: false
    }

}
