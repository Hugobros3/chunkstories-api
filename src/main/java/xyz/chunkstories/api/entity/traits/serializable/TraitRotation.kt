//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import org.joml.Vector2f
import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asArray
import xyz.chunkstories.api.content.json.asFloat
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

class TraitRotation(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitRotation.RotationUpdate> {
    override val traitName = "rotation"

    var yaw = 0f
        private set(value) {
            if (value.isNaN()) {
                entity.world.gameInstance.logger.warn("Tried to set TraitRotation.x to NaN $entity")
            } else {
                field = value
            }
        }
    var pitch = 0f
        private set(value) {
            if (value.isNaN()) {
                entity.world.gameInstance.logger.warn("Tried to set TraitRotation.y to NaN $entity")
            } else {
                field = value
            }
        }

    private val rotationImpulse = Vector2f()

    /** @return A vector3d for the direction
     */
    val directionLookingAt: Vector3dc
        get() {
            val direction = Vector3d()

            val horizontalRotationRad = (yaw / 360f).toDouble() * 2.0 * Math.PI
            val verticalRotationRad = (pitch / 360f).toDouble() * 2.0 * Math.PI
            return eulerXYtoVec3(direction, horizontalRotationRad, verticalRotationRad)
        }

    val upDirection: Vector3dc
        get() {
            val direction = Vector3d(0.0, 1.0, 0.0)

            val horizontalRotationRad = (yaw / 360f).toDouble() * 2.0 * Math.PI
            val verticalRotationRad = ((pitch + 90f) / 360f).toDouble() * 2.0 * Math.PI
            return eulerXYtoVec3(direction, horizontalRotationRad, verticalRotationRad)
        }

    private fun eulerXYtoVec3(direction: Vector3d, horizontalRotationRad: Double, verticalRotationRad: Double): Vector3dc {
        direction.x = Math.sin(horizontalRotationRad) * Math.cos(verticalRotationRad)
        direction.y = Math.sin(verticalRotationRad)
        direction.z = Math.cos(horizontalRotationRad) * Math.cos(verticalRotationRad)

        return direction.normalize()
    }

    fun setRotation(horizontalAngle: Double, verticalAngle: Double) {
        this.yaw = (360 + horizontalAngle).toFloat() % 360
        this.pitch = verticalAngle.toFloat()

        if (pitch > 90)
            pitch = 90f
        if (pitch < -90)
            pitch = -90f

        sendMessageAllSubscribers(RotationUpdate(yaw, pitch))
    }

    fun addRotation(d: Double, e: Double) {
        setRotation(yaw + d, pitch + e)
    }

    /** Sends the view flying about  */
    fun applyInpulse(inpulseHorizontal: Double, inpulseVertical: Double) {
        rotationImpulse.add(Vector2f(inpulseHorizontal.toFloat(), inpulseVertical.toFloat()))
    }

    /** Reduces the acceleration and returns it  */
    fun tickInpulse(): Vector2f {
        rotationImpulse.mul(0.50f)
        if (rotationImpulse.length() < 0.05)
            rotationImpulse.set(0.0f, 0.0f)
        return rotationImpulse
    }

    data class RotationUpdate(val yaw: Float, val pitch: Float) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeFloat(yaw)
            dos.writeFloat(pitch)
        }
    }

    override fun readMessage(dis: DataInputStream) = RotationUpdate(dis.readFloat(), dis.readFloat())

    override fun processMessage(message: RotationUpdate, player: Player?) {
        if (entity.world is WorldMaster && player != entity.controller) {
            //throw Exception("Security violation: Someone tried to update an entity they don't control !")
            //Because of lag this might have been legit, so just reject it for now
            return
        }

        yaw = message.yaw
        pitch = message.pitch

        // Position updates received by the server should be told to everyone but the controller
        if (entity.world is WorldMaster)
            sendMessageAllSubscribersButController(message)
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {

    }

    override fun serialize(): Json = Json.Array(listOf(Json.Value.Number(yaw.toDouble()), Json.Value.Number(pitch.toDouble())))

    override fun deserialize(json: Json) {
        json.asArray?.let { yaw = it[0].asFloat!! ; pitch = it[1].asFloat!! }
    }
}
