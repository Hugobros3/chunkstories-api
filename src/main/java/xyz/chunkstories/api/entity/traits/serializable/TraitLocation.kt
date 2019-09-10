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
import xyz.chunkstories.api.events.entity.EntityTeleportEvent
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.net.packets.PacketEntity
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.chunk.Chunk
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.concurrent.locks.ReentrantLock

/** Holds the information about an entity whereabouts and a flag to mark it as
 * unspawned  */
class TraitLocation(entity: Entity, private val actualLocation: Location) : Trait(entity), TraitSerializable, TraitNetworked<TraitLocation.LocationUpdate> {
    override val traitName = "location"

    private val world: World = actualLocation.world

    private val lock = ReentrantLock()

    var chunk: Chunk? = null
        private set

    private var spawned = false
    private var removed = false

    fun set(location: Location) {
        if (location.world !== this.world)
            throw RuntimeException("Entities can't teleport between worlds directly.")

        set(location as Vector3dc)
    }

    fun set(position: Vector3dc) {
        set(position.x(), position.y(), position.z())
    }

    operator fun set(x: Double, y: Double, z: Double) {
        val event = EntityTeleportEvent(entity, Location(world, x, y, z))
        entity.world.gameContext.pluginManager.fireEvent(event)

        try {
            lock.lock()
            this.actualLocation.x = x
            this.actualLocation.y = y
            this.actualLocation.z = z

            sanitize()
        } finally {
            lock.unlock()
        }

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

        try {
            lock.lock()
            actualLocation.x = actualLocation.x() + dx
            actualLocation.y = actualLocation.y() + dy
            actualLocation.z = actualLocation.z() + dz

            sanitize()/**/
        } finally {
            lock.unlock()
        }

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

    override fun processMessage(message: LocationUpdate, from: Interlocutor) {
        if (world is WorldMaster && from != entity.traits[TraitControllable::class]?.controller) {
            //throw Exception("Security violation: Someone tried to update an entity they don't control !")
            //Because of lag this might have been legit, so just reject it for now
            return
        }

        try {
            lock.lock()
            this.actualLocation.x = message.x
            this.actualLocation.y = message.y
            this.actualLocation.z = message.z

            sanitize()
        } finally {
            lock.unlock()
        }

        // Position updates received by the server should be told to everyone but the controller
        if (world is WorldMaster)
            sendMessageAllSubscribersButController(message)
    }

    /** Prevents entities from going outside the world area and updates the
     * parentHolder reference  */
    private fun sanitize(): Boolean {
        val worldSize = world.worldSize

        actualLocation.x = actualLocation.x() % worldSize
        actualLocation.z = actualLocation.z() % worldSize

        // Loop arround the world
        if (actualLocation.x() < 0)
            actualLocation.x = actualLocation.x() + worldSize
        else if (actualLocation.x() > worldSize)
            actualLocation.x = actualLocation.x() % worldSize

        if (actualLocation.z() < 0)
            actualLocation.z = actualLocation.z() + worldSize
        else if (actualLocation.z() > worldSize)
            actualLocation.z = actualLocation.z() % worldSize

        if (actualLocation.y < 0)
            actualLocation.y = 0.0

        if (actualLocation.y > world.maxHeight)
            actualLocation.y = world.maxHeight.toDouble()

        // Get local chunk co-ordinate
        val chunkX = actualLocation.x().toInt() shr 5
        val chunkY = actualLocation.y().toInt() shr 5
        val chunkZ = actualLocation.z().toInt() shr 5

        // Don't touch updates once the entity was removed
        if (removed /* !entity.exists() */)
            return false

        // Entities not in the world should never be added to it
        if (!spawned)
            return false

        return if (chunk != null && chunk!!.chunkX == chunkX && chunk!!.chunkY == chunkY && chunk!!.chunkZ == chunkZ) {
            false // Nothing to do !
        } else {
            if (chunk != null)
                chunk!!.removeEntity(entity)

            chunk = world.chunksManager.getChunk(chunkX, chunkY, chunkZ)
            // When the region is loaded, add this entity to it.
            if (chunk != null)
            // && regionWithin.isDiskDataLoaded())
                chunk!!.addEntity(entity)

            true
        }
    }

    override fun tick() {
        if (get().x.isNaN() || get().y.isNaN() || get().z.isNaN()) {
            world.gameContext.logger().warn("Entity $entity had invalid location: ${get()}, resetting to world spawn")
            set(world.defaultSpawnLocation)
        }
        //Shouldn't be necessary !
        //sanitize()
    }

    fun onRemoval() {
        try {
            lock.lock()

            if (chunk != null)
                chunk!!.removeEntity(entity)

            removed = true
        } finally {
            lock.unlock()
        }

        // Tell anyone still subscribed to this entity to sod off
        entity.subscribers.forEach { subscriber ->
            subscriber.pushPacket(PacketEntity.createKillerPacket(entity))
            subscriber.unsubscribe(entity)
        }
    }

    fun wasRemoved(): Boolean {
        return removed
    }

    fun onSpawn() {
        try {
            lock.lock()
            spawned = true

            sanitize()
        } finally {
            lock.unlock()
        }
    }

    fun hasSpawned(): Boolean {
        return spawned
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