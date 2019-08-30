//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.net.packets.PacketVelocityDelta
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class TraitVelocity(entity: Entity) : TraitSerializable(entity) {

    val velocity = Vector3d()

    fun setVelocity(velocity: Vector3dc) {
        this.velocity.x = velocity.x()
        this.velocity.y = velocity.y()
        this.velocity.z = velocity.z()

        this.pushComponentEveryone()
    }

    fun setVelocity(x: Double, y: Double, z: Double) {
        this.velocity.x = x
        this.velocity.y = y
        this.velocity.z = z

        this.pushComponentEveryone()
    }

    fun setVelocityX(x: Double) {
        this.velocity.x = x

        this.pushComponentEveryone()
    }

    fun setVelocityY(y: Double) {
        this.velocity.y = y

        this.pushComponentEveryone()
    }

    fun setVelocityZ(z: Double) {
        this.velocity.z = z

        this.pushComponentEveryone()
    }

    fun addVelocity(delta: Vector3dc) {
        this.velocity.add(delta)

        this.pushComponentEveryoneButController()

        // Notify the controller in a special way so they don't experience lag
        // due to being set back at a previous velocity
        entity.traits[TraitControllable::class]?.let { ecc ->
            val controller = ecc.controller
            if (controller != null) {
                val packet = PacketVelocityDelta(entity.world, delta)
                controller.pushPacket(packet)
            }
        }
    }

    fun addVelocity(x: Double, y: Double, z: Double) {
        this.addVelocity(Vector3d(x, y, z))
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeDouble(velocity.x())
        dos.writeDouble(velocity.y())
        dos.writeDouble(velocity.z())
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        velocity.x = dis.readDouble()
        velocity.y = dis.readDouble()
        velocity.z = dis.readDouble()

        this.pushComponentEveryoneButController()
    }

    override fun tick() {
        if (velocity.x.isNaN() || velocity.y.isNaN() || velocity.z.isNaN()) {
            entity.world.gameContext.logger().warn("Entity $entity had invalid velocity: $velocity, resetting to zero")
            velocity.set(0.0, 0.0, 0.0)
        }
    }
}
