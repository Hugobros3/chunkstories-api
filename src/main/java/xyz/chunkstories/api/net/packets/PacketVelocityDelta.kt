//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.net.packets

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.client.IngameClient
import xyz.chunkstories.api.entity.traits.serializable.TraitVelocity
import xyz.chunkstories.api.net.PacketWorld
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.entityIfIngame
import xyz.chunkstories.api.util.VoidAction
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream

/** When adding momentum to a controlled entity (ie the player's current
 * entity), doing so by sending the client it's new velocity directly would
 * could result in weird, laggy behavior when the client is rapidly changing
 * direction ( it's new velocity would be based on his own, RTT-seconds ago ).
 * To avoid this we use a dedicated packet which entire purpose is to tell a
 * player to offset it's velocity by N  */
// TODO this should be handled by TraitVelocity or smth
class PacketVelocityDelta : PacketWorld {
    constructor(world: World) : super(world)
    constructor(world: World, delta: Vector3dc) : super(world) {
        this.delta = delta
    }

    private lateinit var delta: Vector3dc
    override fun send(dos: DataOutputStream) {
        dos.writeDouble(delta.x())
        dos.writeDouble(delta.y())
        dos.writeDouble(delta.z())
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        val delta = Vector3d(dis.readDouble(), dis.readDouble(), dis.readDouble())
        val entity = (world.gameInstance as IngameClient).player.entityIfIngame ?: return
        entity.traits[TraitVelocity::class]?.addVelocity(delta)
    }
}