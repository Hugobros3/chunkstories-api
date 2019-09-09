//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asBoolean
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

/** Keeps track of the flying flag, movement logic has moved to PlayerMovementController in core */
class TraitFlyingMode(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitFlyingMode.FlyModeUpdate> {
    override val serializedTraitName = "flyMode"

    var isAllowed: Boolean = false
        set(value) {
            field = value
            if (entity.world is WorldMaster)
                sendMessageController(FlyModeUpdate.SetAllow(field))
        }
    var isFlying: Boolean = false
        set(value) {
            field = value
            if (entity.world is WorldMaster)
                sendMessageController(FlyModeUpdate.SetFlying(field))
            else
                sendMessageAllSubscribers(FlyModeUpdate.SetFlying(field))
        }

    sealed class FlyModeUpdate : TraitMessage() {
        data class SetAllow(val value: Boolean) : FlyModeUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.write(if (value) 1 else 0)
            }
        }

        data class SetFlying(val value: Boolean) : FlyModeUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.write(if (value) 3 else 2)
            }
        }
    }

    override fun readMessage(dis: DataInputStream): FlyModeUpdate {
        return when (dis.read()) {
            0 -> FlyModeUpdate.SetAllow(false)
            1 -> FlyModeUpdate.SetAllow(true)
            2 -> FlyModeUpdate.SetFlying(false)
            3 -> FlyModeUpdate.SetFlying(true)
            else -> throw Exception()
        }
    }

    override fun processMessage(message: FlyModeUpdate, from: Interlocutor) {
        when (message) {
            is FlyModeUpdate.SetAllow -> {
                if (entity.world is WorldMaster) {
                    return // Users cannot set that directly
                }

                isAllowed = message.value
            }
            is FlyModeUpdate.SetFlying -> {
                if (isAllowed) {
                    isFlying = message.value
                }
            }
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if(subscriber == entity.traits[TraitControllable::class]?.controller) {
            sendMessageController(FlyModeUpdate.SetAllow(isAllowed))
            sendMessageController(FlyModeUpdate.SetFlying(isFlying))
        }
    }

    override fun serialize() = Json.Dict(mapOf(
                "allow" to Json.Value.Bool(isAllowed),
                "flying" to Json.Value.Bool(isFlying)))

    override fun deserialize(json: Json) {
        val dict = json.asDict ?: return
        isAllowed = dict["allow"].asBoolean ?: false
        isFlying = dict["flying"].asBoolean ?: false
    }
}