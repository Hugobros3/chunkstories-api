//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.net.PacketWorld
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

/** The server just tells the time  */
class PacketTime(world: World) : PacketWorld(world) {
    var tod = 0.0f
    var overcastFactor = 0f

    override fun send(dos: DataOutputStream) {
        dos.writeFloat(tod)
        dos.writeFloat(overcastFactor)
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        tod = dis.readFloat()
        overcastFactor = dis.readFloat()
        if (world !is WorldMaster) {
            world.sky = world.sky.copy(timeOfDay = tod)
            // TODO handle weather and such
            //world.setSunCycle(time)
            //world.setWeather(overcastFactor)
        }
    }
}