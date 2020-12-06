//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.entity.traits.serializable.TraitMessage
import xyz.chunkstories.api.entity.traits.serializable.TraitNetworked
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.player.IngamePlayer
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

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
        fun <M> createUpdatePacket(entity: Entity, trait: TraitNetworked<M>, message: M) where M : TraitMessage = PacketEntity(entity, trait as Trait, message)
        fun createKillerPacket(entity: Entity) = PacketEntity(entity, true)
    }

    override fun send(dos: DataOutputStream) {
        val entityID = entity.id
        val entityTypeID = entity.world.gameInstance.contentTranslator.getIdForEntity(entity).toShort()

        dos.writeLong(entityID)
        dos.writeShort(entityTypeID.toInt())

        dos.writeBoolean(killerPacket)

        if (!killerPacket) {
            dos.writeInt(trait.id)
            message.write(dos)
        }
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        val entityID = dis.readLong()
        val entityTypeID = dis.readShort()

        val removeEntity = dis.readBoolean()

        if (entityTypeID.toInt() == -1)
            return

        var entity = world.getEntity(entityID)
        var freshlyCreatedEntity = false

        if (entity == null) {
            if (world is WorldMaster) {
                player!!.sendMessage("You are sending packets to the server about a removed entity. Ignoring those.")
                return
            } else if (!removeEntity) {
                entity = world.gameInstance.contentTranslator.getEntityForId(entityTypeID.toInt())!!.newEntity(world) // This is technically
                entity.id = entityID
                freshlyCreatedEntity = true
            }
        }

        if (removeEntity) {
            world.removeEntity(entityID)
        } else {
            val traitId = dis.readInt()
            trait = entity!!.traits.byId[traitId]
            message = (trait as TraitNetworked<*>).readMessage(dis)
            @Suppress("UNCHECKED_CAST")
            (trait as TraitNetworked<TraitMessage>).processMessage(message, player as? IngamePlayer)

            if (freshlyCreatedEntity) {
                // Only the WorldMaster is allowed to spawn new entities in the world
                if (world is WorldMaster)
                    world.addEntity(entity)
            }
        }
    }
}
