//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.concurrent.locks.ReentrantLock

import org.joml.Vector3dc

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.entity.EntityTeleportEvent
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.chunk.Chunk
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

/** Holds the information about an entity whereabouts and a flag to mark it as
 * unspawned  */
class TraitLocation(entity: Entity, private val pos: Location) : TraitSerializable(entity) {

    private val world: World = pos.getWorld()

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
            this.pos.x = x
            this.pos.y = y
            this.pos.z = z

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
        this.pushComponentEveryone()
    }

    fun move(dx: Double, dy: Double, dz: Double) {
        try {
            lock.lock()
            pos.x = pos.x() + dx
            pos.y = pos.y() + dy
            pos.z = pos.z() + dz

            sanitize()/**/
        } finally {
            lock.unlock()
        }

        this.pushComponentEveryone()
    }

    fun move(delta: Vector3dc) {
        move(delta.x(), delta.y(), delta.z())
    }

    /** Copies the location and returns it. The actual location is never mutated
     * outside of set().  */
    fun get(): Location {
        val pos = this.pos
        return Location(pos.getWorld(), pos)
    }

    @Throws(IOException::class)
    public override fun push(to: StreamTarget, dos: DataOutputStream) {
        dos.writeDouble(pos.x())
        dos.writeDouble(pos.y())
        dos.writeDouble(pos.z())
    }

    @Throws(IOException::class)
    public override fun pull(from: StreamSource, dis: DataInputStream) {
        // pos = new Location(world, 0, 0, 0);

        val x = dis.readDouble()
        val y = dis.readDouble()
        val z = dis.readDouble()

        try {
            lock.lock()
            this.pos.x = x
            this.pos.y = y
            this.pos.z = z

            sanitize()
        } finally {
            lock.unlock()
        }

        // Position updates received by the server should be told to everyone but the
        // controller
        if (world is WorldMaster)
            pushComponentEveryoneButController()
    }

    /** Prevents entities from going outside the world area and updates the
     * parentHolder reference  */
    private fun sanitize(): Boolean {
        val worldSize = world.worldSize

        pos.x = pos.x() % worldSize
        pos.z = pos.z() % worldSize

        // Loop arround the world
        if (pos.x() < 0)
            pos.x = pos.x() + worldSize
        else if (pos.x() > worldSize)
            pos.x = pos.x() % worldSize

        if (pos.z() < 0)
            pos.z = pos.z() + worldSize
        else if (pos.z() > worldSize)
            pos.z = pos.z() % worldSize

        if (pos.y < 0)
            pos.y = 0.0

        if (pos.y > world.maxHeight)
            pos.y = world.maxHeight.toDouble()

        // Get local chunk co-ordinate
        val chunkX = pos.x().toInt() shr 5
        val chunkY = pos.y().toInt() shr 5
        val chunkZ = pos.z().toInt() shr 5

        // Don't touch updates once the entity was removed
        if (removed /* !entity.exists() */)
            return false

        // Entities not in the world should never be added to it
        if (!spawned)
            return false

        if (chunk != null && chunk!!.chunkX == chunkX && chunk!!.chunkY == chunkY && chunk!!.chunkZ == chunkZ) {
            return false // Nothing to do !
        } else {
            if (chunk != null)
                chunk!!.removeEntity(entity)

            chunk = world.getChunk(chunkX, chunkY, chunkZ)
            // When the region is loaded, add this entity to it.
            if (chunk != null)
            // && regionWithin.isDiskDataLoaded())
                chunk!!.addEntity(entity)

            return true
        }
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

        this.pushComponentEveryone()

        // Tell anyone still subscribed to this entity to sod off
        entity.subscribers.forEach { subscriber -> subscriber.unsubscribe(entity) }
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
}

fun Entity.move(delta: Vector3dc) = this.traits[TraitLocation::class]?.move(delta)
fun Entity.move(dx: Double, dy: Double, dz: Double) = this.traits[TraitLocation::class]?.move(dx, dy, dz)