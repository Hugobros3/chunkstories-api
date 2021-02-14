//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.net.Packet

/** A subscriber wants to be kept up-to-date with the latest changes in his world */
interface Subscriber {
    fun pushPacket(packet: Packet)
}
