//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.client.net.ClientPacketsProcessor
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.entity.traits.serializable.TraitMessage
import xyz.chunkstories.api.entity.traits.serializable.TraitNetworked
import xyz.chunkstories.api.exceptions.UnknownComponentException
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.server.RemotePlayer
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldMaster

class PacketEntity : PacketWorld {
    private lateinit var entity: Entity
    private lateinit var trait: Trait
    private lateinit var message: TraitMessage
    private var killerPacket = false

    @Suppress("unused")
    constructor(world: World) : super(world)

    private constructor(entity: Entity, trait: Trait, message: TraitMessage) : super(entity.world) {
        this.entity = entity
        this.trait = trait
        this.message = message
    }

    private constructor(entity: Entity, killerPacket: Boolean) : super(entity.world) {
        this.entity = entity
        this.killerPacket = killerPacket
    }

    companion object {
        fun <M> createUpdatePacket(entity: Entity, trait: TraitNetworked<M>, message: M) where M: TraitMessage = PacketEntity(entity, trait as Trait, message)
        fun createKillerPacket(entity: Entity) = PacketEntity(entity, true)
    }

    @Throws(IOException::class)
    override fun send(destinator: PacketDestinator, dos: DataOutputStream, context: PacketSendingContext) {
        val entityUUID = entity.UUID
        val entityTypeID = entity.world.contentTranslator.getIdForEntity(entity).toShort()

        if (destinator !is Subscriber)
            throw Exception("Why on earth")
        //else
        //    deleteEntity = deleteEntity or !entity.subscribers.contains(destinator)

        dos.writeLong(entityUUID)
        dos.writeShort(entityTypeID.toInt())

        dos.writeBoolean(killerPacket)

        if (!killerPacket) {
            dos.writeInt(trait.id)
            message.write(dos)
        }
    }

    @Throws(IOException::class, UnknownComponentException::class)
    override fun process(sender: PacketSender, dis: DataInputStream, processor: PacketReceptionContext) {
        val entityUUID = dis.readLong()
        val entityTypeID = dis.readShort()

        val killerPacket = dis.readBoolean()

        if (entityTypeID.toInt() == -1)
            return

        val world = processor.world ?: return

        var entity = world.getEntityByUUID(entityUUID)
        var shouldCreateEntity = false

        if (entity == null) {
            if (world is WorldMaster && sender is RemotePlayer) {
                (sender as Player).sendMessage("You are sending packets to the server about a removed entity. Ignoring those.")
                return
            } else if (!killerPacket) {
                entity = world.contentTranslator.getEntityForId(entityTypeID.toInt())!!.newEntity(world) // This is technically

                entity.UUID = entityUUID
                shouldCreateEntity = true
            }
        }

        val traitId = dis.readInt()
        trait = entity!!.traits.byId[traitId]
        message = (trait as TraitNetworked<*>).readMessage(dis)
        (trait as TraitNetworked<TraitMessage>).processMessage(message, sender as Interlocutor)

        if (shouldCreateEntity && !killerPacket) {
            // Only the WorldMaster is allowed to spawn new entities in the world
            if (processor is ClientPacketsProcessor)
                processor.world.addEntity(entity)
        }

        if (killerPacket) {
            world.removeEntity(entity)
        }
    }
}
