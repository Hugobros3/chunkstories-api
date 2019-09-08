//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asArray
import xyz.chunkstories.api.content.json.asDouble
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.net.packets.PacketVelocityDelta
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream

class TraitVelocity(entity: Entity) : TraitSerializable(entity), TraitNetworked<TraitVelocity.VelocityUpdate> {
    val velocity = Vector3d()

    fun setVelocity(velocity: Vector3dc) = setVelocity(velocity.x(), velocity.y(), velocity.z())

    fun setVelocity(x: Double, y: Double, z: Double) {
        this.velocity.x = x
        this.velocity.y = y
        this.velocity.z = z

        sendMessageAllSubscribers(VelocityUpdate.Absolute(velocity.x, velocity.y, velocity.z))
    }

    fun addVelocity(velocity: Vector3dc) = addVelocity(velocity.x(), velocity.y(), velocity.z())

    fun addVelocity(dx: Double, dy: Double, dz: Double) {
        this.velocity.add(dx, dy, dz)

        sendMessageAllSubscribers(VelocityUpdate.Absolute(velocity.x, velocity.y, velocity.z))

        // Notify the controller in a special way so they don't experience lag
        // due to being set back at a previous velocity
        sendMessageController(VelocityUpdate.Relative(dx, dy, dz))
    }

    sealed class VelocityUpdate : TraitMessage() {
        data class Absolute(val x: Double, val y: Double, val z: Double) : VelocityUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.writeByte(0)
                dos.writeDouble(this.x)
                dos.writeDouble(this.y)
                dos.writeDouble(this.z)
            }
        }
        data class Relative(val dx: Double, val dy: Double, val dz: Double) : VelocityUpdate() {
            override fun write(dos: DataOutputStream) {
                dos.writeByte(1)
                dos.writeDouble(this.dx)
                dos.writeDouble(this.dy)
                dos.writeDouble(this.dz)
            }
        }
    }

    override fun readMessage(dis: DataInputStream): VelocityUpdate {
        return when(dis.read()) {
            0 -> VelocityUpdate.Absolute(dis.readDouble(), dis.readDouble(), dis.readDouble())
            1 -> VelocityUpdate.Relative(dis.readDouble(), dis.readDouble(), dis.readDouble())
            else -> throw Exception()
        }
    }

    override fun processMessage(message: VelocityUpdate, from: Interlocutor) {
        if (entity.world is WorldMaster && from != entity.traits[TraitControllable::class]?.controller) {
            //throw Exception("Security violation: Someone tried to update an entity they don't control !")
            //Because of lag this might have been legit, so just reject it for now
            return
        }

        when(message) {
            is VelocityUpdate.Absolute -> {
                velocity.x = message.x
                velocity.y = message.y
                velocity.z = message.z
            }
            is VelocityUpdate.Relative -> {
                velocity.x += message.dx
                velocity.y += message.dy
                velocity.z += message.dz
            }
        }

        // Position updates received by the server should be told to everyone but the controller
        if (entity.world is WorldMaster)
            sendMessageAllSubscribersButController(message)
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        sendMessage(subscriber, VelocityUpdate.Absolute(velocity.x, velocity.y, velocity.z))
    }

    override fun tick() {
        if (velocity.x.isNaN() || velocity.y.isNaN() || velocity.z.isNaN()) {
            entity.world.gameContext.logger().warn("Entity $entity had invalid velocity: $velocity, resetting to zero")
            velocity.set(0.0, 0.0, 0.0)
        }
    }

    override fun serialize(): Json {
        return Json.Array(listOf(Json.Value.Number(velocity.x), Json.Value.Number(velocity.y), Json.Value.Number(velocity.z)))
    }

    override fun deserialize(json: Json) {
        json.asArray?.let { velocity.x = it[0].asDouble!! ; velocity.y = it[1].asDouble!! ; velocity.z = it[2].asDouble!! }
    }
}
