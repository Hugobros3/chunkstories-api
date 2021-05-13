//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net

import xyz.chunkstories.api.Engine
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.server.UserConnection
import xyz.chunkstories.api.world.GameInstance
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream

typealias PacketId = Int

abstract class Packet(val engine: Engine) {
    abstract fun send(dos: DataOutputStream)
    abstract fun receive(dis: DataInputStream, user: UserConnection?)
}

abstract class PacketWorld(val world: World): Packet(world.gameInstance.engine) {
    final override fun receive(dis: DataInputStream, user: UserConnection?) = throw Exception("Call with the player argument instead")
    abstract fun receive(dis: DataInputStream, player: Player?)
}