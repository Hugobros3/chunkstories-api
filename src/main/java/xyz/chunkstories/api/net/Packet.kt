//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.net

import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream

abstract class Packet {
    abstract fun send(dos: DataOutputStream)
    abstract fun receive(dis: DataInputStream, player: Player?)
}

abstract class PacketWorld(val world: World): Packet()