//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import org.joml.Matrix4f
import org.joml.Vector3d
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.TraitRenderable
import xyz.chunkstories.api.entity.traits.serializable.*
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldClient
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.abs

class EntityDroppedItem(definition: EntityDefinition, world: World) : Entity(definition, world), InventoryOwner {
    protected var rotation = 0f

    val entityVelocity = TraitVelocity(this)
    val collisions = TraitCollidable(this)

    var spawnTime: Long = 0

    val container = TraitItemContainer(this)
    //val inventory = TraitInventory(this, 1, 1, true)

    init {
        DroppedItemRenderer(this)
        spawnTime = System.currentTimeMillis()
    }

    fun canBePickedUpYet(): Boolean {
        return System.currentTimeMillis() - spawnTime > 1000L
    }

    override fun tick() {
        val velocity = entityVelocity.velocity

        if (world is WorldMaster) {
            val inWater = world.peek(location).voxel.liquid

            val terminalVelocity = if (inWater) -0.25 else -0.5
            if (velocity.y() > terminalVelocity && !collisions.isOnGround)
                velocity.y = velocity.y() - 0.016
            if (velocity.y() < terminalVelocity)
                velocity.y = terminalVelocity

            val remainingToMove = collisions.moveWithCollisionRestrain(velocity.x(), velocity.y(), velocity.z())
            if (remainingToMove.y() < -0.02 && collisions.isOnGround) {
                if (remainingToMove.y() < -0.01) {
                    // Bounce
                    val originalDownardsVelocity = velocity.y()
                    val bounceFactor = 0.15
                    velocity.mul(bounceFactor)
                    velocity.y = -originalDownardsVelocity * bounceFactor

                    // world.getSoundManager().playSoundEffect("./sounds/dogez/weapon/grenades/grenade_bounce.ogg",
                    // Mode.NORMAL, getLocation(), 1, 1, 10, 35);
                } else
                    velocity.mul(0.0)
            }

            velocity.x *= 0.98
            velocity.z *= 0.98

            if (abs(velocity.x) < 0.02)
                velocity.x = 0.0
            if (abs(velocity.z) < 0.02)
                velocity.z = 0.0

            if (abs(velocity.y) < 0.01)
                velocity.y = 0.0

            entityVelocity.setVelocity(velocity)
        }

        if (world is WorldClient) {

            if (collisions.isOnGround) {
                rotation += 1.0f
                rotation %= 360f
            }
        }
    }

    override fun getBoundingBox(): Box {
        return Box.fromExtentsCenteredHorizontal(0.5, 0.75, 0.5)
    }

    companion object {
        fun spawn(item: Item, amount: Int, location: Location, initialVelocity: Vector3d = Vector3d(0.0)) {
            val thrownItem = location.world.content.entities.getEntityDefinition("droppedItem")!!.newEntity<EntityDroppedItem>(location.world)
            thrownItem.traitLocation.set(location)
            thrownItem.entityVelocity.setVelocity(initialVelocity)
            thrownItem.traits[TraitItemContainer::class]!!.let {
                it.item = item
                it.amount = amount
            }
            //thrownItem.traits[TraitInventory::class]!!.inventory.addItem(item, amount)
            location.world.addEntity(thrownItem)
        }
    }
}

class TraitItemContainer(entity: Entity) : TraitSerializable(entity), TraitNetworked<TraitItemContainer.DroppedItemUpdate> {
    var item: Item? = null
    var amount: Int = 0

    override fun serialize() = serializeItemAndAmount(item, amount)

    override fun deserialize(json: Json) {
        val (item, amount) = deserializeItemAndAmount(entity.world.contentTranslator, json)
        this.item = item
        this.amount = amount
    }

    data class DroppedItemUpdate(val item: Item?, val amount: Int) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeUTF(serializeItemAndAmount(item, amount).stringSerialize())
        }
    }

    override fun readMessage(dis: DataInputStream) = deserializeItemAndAmount(entity.world.contentTranslator, dis.readUTF().toJson()).let {
        DroppedItemUpdate(it.first, it.second)
    }

    override fun processMessage(message: DroppedItemUpdate, from: Interlocutor) {
        if (entity.world is WorldMaster)
            return

        item = message.item
        amount = message.amount
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        sendMessage(subscriber, DroppedItemUpdate(item, amount))
    }
}

class DroppedItemRenderer(private val entityGroundItem: EntityDroppedItem) : TraitRenderable<EntityDroppedItem>(entityGroundItem), InventoryOwner {

    override fun buildRepresentation(representationsGobbler: RepresentationsGobbler) {
        val pile = entity.traits[TraitItemContainer::class] ?: return
        //val item = entity.traits[ItemOnGroundContents::class]?.i ?: return //TODO show some error

        val dt = (entityGroundItem.spawnTime - System.currentTimeMillis()).toInt() / 1000.0

        val matrix = Matrix4f()
        matrix.translate(entity.location.toVec3f())
        matrix.translate(0f, 0.5f + 0.25f * Math.sin(dt).toFloat(), 0f)
        matrix.rotate(dt.toFloat(), 0f, 1f, 0f)
        pile.item?.buildRepresentation(matrix, representationsGobbler)
    }
}