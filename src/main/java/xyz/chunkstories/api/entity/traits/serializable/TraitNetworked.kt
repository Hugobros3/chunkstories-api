//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.net.packets.PacketEntity
import xyz.chunkstories.api.player.IngamePlayer
import xyz.chunkstories.api.world.WorldClient
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

interface TraitNetworked<M : TraitMessage> {
    val entity: Entity

    @Throws(IOException::class)
    fun readMessage(dis: DataInputStream): M

    fun processMessage(message: M, player: IngamePlayer?)

    fun sendMessage(subscriber: Subscriber, message: M) {
        val packet = PacketEntity.createUpdatePacket(entity, this, message)
        subscriber.pushPacket(packet)
    }

    fun sendMessageAllSubscribers(message: M) {
        for (subscriber in entity.subscribers) {
            sendMessage(subscriber, message)
        }
    }

    fun sendMessageAllSubscribersButController(message: M) {
        val controller = entity.traits[TraitControllable::class]?.controller
        for (subscriber in entity.subscribers) {
            if (subscriber != controller)
                sendMessage(subscriber, message)
        }
    }

    /** In the context of a server and a remote player acting as a controller, send a message to that player. Will not send a message to the local player in singleplayer. */
    fun sendMessageController(message: M) {
        if (entity.world is WorldClient)
            return
        val controller = entity.traits[TraitControllable::class]?.controller ?: return
        sendMessage(controller, message)
    }

    fun whenSubscriberRegisters(subscriber: Subscriber)

    fun whenSubscriberUnregisters(subscriber: Subscriber) {}
}

abstract class TraitMessage {
    @Throws(IOException::class)
    abstract fun write(dos: DataOutputStream)
}