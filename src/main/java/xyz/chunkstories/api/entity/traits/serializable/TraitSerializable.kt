//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.net.packets.PacketEntity
import xyz.chunkstories.api.util.Generalized
import xyz.chunkstories.api.util.SerializedName
import xyz.chunkstories.api.world.serialization.OfflineSerializedData
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

@Generalized
abstract class TraitSerializable(entity: Entity) : Trait(entity) {
    /** Reflects the name declared in the @SerializedName annotation, or the the top
     * level class name if none is declared  */
    val traitName: String

    init {
        val serializedNameAnnotations = this.javaClass.getAnnotationsByType(SerializedName::class.java)
        traitName = if (serializedNameAnnotations.isNotEmpty())
            serializedNameAnnotations[0].name
        else
            this.javaClass.name
    }

    /** Push will tell all subscribers of the entity about a change of this
     * component only  */
    /*fun pushComponentEveryone() {
        entity.subscribers.forEach { pushComponent(it) }
    }

    /** Push the component to the controller, if such one exists  */
    fun pushComponentController() {
        val controller = entity.traits[TraitControllable::class]?.controller ?: return
        pushComponent(controller)
    }

    /** Push the component to everyone but the controller, if such one exists  */
    fun pushComponentEveryoneButController() {
        val controller = entity.traits[TraitControllable::class]?.controller

        for (subscriber in entity.subscribers) {
            // Don't push the update to the controller.
            if (subscriber == controller)
                continue

            this.pushComponent(subscriber)
        }
    }

    /** Push the component to someone in particular  */
    fun pushComponent(subscriber: Subscriber) {
        // You may check that subscriber has subscribed to said entity ?
        // A: nope because we send the EntityExistence (hint: false) component to [just]
        // unsubscribed guys so it wouldn't work

        // TODO rework that assumption now

        val packet = PacketEntity(entity, this)
        // this.pushComponentInStream(subscriber, packet.getSynchPacketOutputStream());
        subscriber.pushPacket(packet)
    }

    @Throws(IOException::class)
    open fun pushComponentInStream(to: StreamTarget, dos: DataOutputStream) {
        // Offline saves will have version discrepancies, so we use a symbolic name
        // instead
        if (to is OfflineSerializedData) {
            dos.writeInt(-1)
            dos.writeUTF(this.traitName)
        } else {
            dos.writeInt(this.id)
        }

        // Push actual component data
        push(to, dos)
    }

    @Throws(IOException::class)
    fun tryPull(from: StreamSource, dis: DataInputStream) {
        pull(from, dis)
    }*/

    /*@Throws(IOException::class)
    protected abstract fun push(destinator: StreamTarget, dos: DataOutputStream)

    @Throws(IOException::class)
    protected abstract fun pull(from: StreamSource, dis: DataInputStream)*/

    @Throws(IOException::class)
    protected abstract fun serialize() : Json

    @Throws(IOException::class)
    protected abstract fun deserialize(json: Json)
}
