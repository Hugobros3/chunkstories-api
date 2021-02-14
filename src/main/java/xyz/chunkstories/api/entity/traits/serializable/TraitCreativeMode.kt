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
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.block.MiningTool
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

class TraitCreativeMode(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitCreativeMode.CreativeModeUpdate> {
    override val traitName = "creativeMode"

    var enabled: Boolean = false
        set(value) {
            field = value
            // Only the controller cares about his creative status
            sendMessageController(CreativeModeUpdate(value))
        }

    companion object {
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

    override fun processMessage(message: CreativeModeUpdate, player: Player?) {
        if(entity.world is WorldMaster) {
            return // players should use commands to toggle that rather
        }

        enabled = message.state
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        // Inform the controller of his status
        if(subscriber == entity.controller)
            sendMessage(subscriber, CreativeModeUpdate(enabled))
    }

    override fun serialize() = Json.Value.Bool(enabled)

    override fun deserialize(json: Json) {
        enabled = json.asBoolean ?: false
    }

}
