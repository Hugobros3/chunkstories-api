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
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

class TraitVelocity(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitVelocity.VelocityUpdate> {
    override val traitName = "velocity"
    private val realVelocity = Vector3d()
    val velocity: Vector3dc
        get() = Vector3d(realVelocity)

    fun setVelocity(velocity: Vector3dc) = setVelocity(velocity.x(), velocity.y(), velocity.z())

    fun setVelocity(x: Double, y: Double, z: Double) {
        this.realVelocity.x = x
        this.realVelocity.y = y
        this.realVelocity.z = z

        sendMessageAllSubscribers(VelocityUpdate.Absolute(realVelocity.x, realVelocity.y, realVelocity.z))
    }

    fun setVelocityX(x: Double) {
        setVelocity(x, realVelocity.y, realVelocity.z)
    }

    fun setVelocityY(y: Double) {
        setVelocity(realVelocity.x, y, realVelocity.z)
    }

    fun setVelocityZ(z: Double) {
        setVelocity(realVelocity.x, realVelocity.y, z)
    }

    fun addVelocity(velocity: Vector3dc) = addVelocity(velocity.x(), velocity.y(), velocity.z())

    fun addVelocity(dx: Double, dy: Double, dz: Double) {
        this.realVelocity.add(dx, dy, dz)

        sendMessageAllSubscribers(VelocityUpdate.Absolute(realVelocity.x, realVelocity.y, realVelocity.z))

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
        return when (dis.read()) {
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

        when (message) {
            is VelocityUpdate.Absolute -> {
                realVelocity.x = message.x
                realVelocity.y = message.y
                realVelocity.z = message.z
            }
            is VelocityUpdate.Relative -> {
                realVelocity.x += message.dx
                realVelocity.y += message.dy
                realVelocity.z += message.dz
            }
        }

        // Position updates received by the server should be told to everyone but the controller
        if (entity.world is WorldMaster)
            sendMessageAllSubscribersButController(message)
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        sendMessage(subscriber, VelocityUpdate.Absolute(realVelocity.x, realVelocity.y, realVelocity.z))
    }

    override fun tick() {
        if (realVelocity.x.isNaN() || realVelocity.y.isNaN() || realVelocity.z.isNaN()) {
            entity.world.gameContext.logger().warn("Entity $entity had invalid velocity: $realVelocity, resetting to zero")
            realVelocity.set(0.0, 0.0, 0.0)
        }
    }

    override fun serialize(): Json {
        return Json.Array(listOf(Json.Value.Number(realVelocity.x), Json.Value.Number(realVelocity.y), Json.Value.Number(realVelocity.z)))
    }

    override fun deserialize(json: Json) {
        json.asArray?.let { realVelocity.x = it[0].asDouble!!; realVelocity.y = it[1].asDouble!!; realVelocity.z = it[2].asDouble!! }
    }
}
