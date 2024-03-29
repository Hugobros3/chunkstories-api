//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import org.joml.Vector3dc
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asArray
import xyz.chunkstories.api.content.json.asDouble
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

/** Holds the information about an entity whereabouts and a flag to mark it as unspawned  */
class TraitLocation(entity: Entity, private val actualLocation: Location) : Trait(entity), TraitSerializable, TraitNetworked<TraitLocation.LocationUpdate> {
    override val traitName = "location"

    fun set(location: Location) {
        if (location.world !== entity.world)
            throw RuntimeException("Entities can't teleport between worlds directly.")
        set(location as Vector3dc)
    }

    fun set(position: Vector3dc) {
        set(position.x(), position.y(), position.z())
    }

    operator fun set(x: Double, y: Double, z: Double) {
        this.actualLocation.x = x
        this.actualLocation.y = y
        this.actualLocation.z = z

        // Push updates to everyone subscribed to this
        // In client mode it means that the controlled entity has the server subscribed
        // so it will update it's status to the server

        // In server/master mode any drastic location change is told to everyone, as
        // setLocation() is not called when the server receives updates
        // from the controller but only when an external event changes the location.
        sendMessageAllSubscribers(LocationUpdate(actualLocation.x, actualLocation.y, actualLocation.z))
    }

    fun move(dx: Double, dy: Double, dz: Double) {
        if (dx.isNaN() || dy.isNaN() || dz.isNaN()) {
            System.err.println("move by nan $dx $dy $dz")
            Thread.dumpStack()
            return
        }

        actualLocation.x = actualLocation.x() + dx
        actualLocation.y = actualLocation.y() + dy
        actualLocation.z = actualLocation.z() + dz

        sendMessageAllSubscribers(LocationUpdate(actualLocation.x, actualLocation.y, actualLocation.z))
    }

    fun move(delta: Vector3dc) {
        move(delta.x(), delta.y(), delta.z())
    }

    /** Copies the location and returns it. The actual location is never mutated
     * outside of set().  */
    fun get(): Location {
        val pos = this.actualLocation
        return Location(pos.world, pos)
    }

    data class LocationUpdate(val x: Double, val y: Double, val z: Double) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeDouble(x)
            dos.writeDouble(y)
            dos.writeDouble(z)
        }
    }

    override fun readMessage(dis: DataInputStream): LocationUpdate =
            LocationUpdate(dis.readDouble(), dis.readDouble(), dis.readDouble())

    override fun processMessage(message: LocationUpdate, player: Player?) {
        if (entity.world is WorldMaster && player != entity.controller) {
            throw Exception("Security violation: Someone tried to update an entity they don't control !")
        }

        this.actualLocation.x = message.x
        this.actualLocation.y = message.y
        this.actualLocation.z = message.z

        // Position updates received by the server should be told to everyone but the controller
        if (entity.world is WorldMaster)
            sendMessageAllSubscribersButController(message)
    }

    override fun tick() {
        if (get().x.isNaN() || get().y.isNaN() || get().z.isNaN()) {
            entity.world.logger.warn("Entity $entity had invalid location: ${get()}, resetting to world spawn")
            set(entity.world.properties.spawn)
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        sendMessage(subscriber, LocationUpdate(actualLocation.x, actualLocation.y, actualLocation.z))
    }

    override fun deserialize(json: Json) {
        val arr = json.asArray ?: return
        actualLocation.set(arr[0].asDouble!!, arr[1].asDouble!!, arr[2].asDouble!!)
    }

    override fun serialize(): Json {
        return Json.Array(listOf(Json.Value.Number(actualLocation.x), Json.Value.Number(actualLocation.y), Json.Value.Number(actualLocation.z)))
    }
}

fun Entity.move(delta: Vector3dc) = this.traits[TraitLocation::class]?.move(delta)
fun Entity.move(dx: Double, dy: Double, dz: Double) = this.traits[TraitLocation::class]?.move(dx, dy, dz)