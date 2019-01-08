//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.components

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.net.packets.PacketVoxelUpdate
import xyz.chunkstories.api.server.RemotePlayer
import xyz.chunkstories.api.world.cell.CellComponents
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

/**
 * Holds extra information on top of the 32 bits every getCell gets.
 * Has to provide it's own serialization routines
 */
abstract class VoxelComponent(val holder: CellComponents) {

    val name: String
        get() = holder.getRegisteredComponentName(this) ?: throw Exception("Assertion broken")

    /** Pushes the component to every client subscribed to the chunk owning this voxel  */
    fun pushComponentEveryone() {
        for (user in holder.chunk.holder().users) {
            if (user is RemotePlayer) {
                pushComponent(user)
            }
        }
    }

    /** Pushes the component to a specific player  */
    fun pushComponent(player: RemotePlayer) {
        val packet = PacketVoxelUpdate(holder.cell, this)
        player.pushPacket(packet)
    }

    /** Serializes this components data as a stream of bytes  */
    @Throws(IOException::class)
    abstract fun push(destinator: StreamTarget, dos: DataOutputStream)

    /** Loads in the component data from a stream of bytes  */
    @Throws(IOException::class)
    abstract fun pull(from: StreamSource, dis: DataInputStream)
}
