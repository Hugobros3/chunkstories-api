//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.net

import xyz.chunkstories.api.net.Packet

class UnknowPacketException(message: String) : Exception(message) {

    constructor(packetType: Int) : this(
            "Unknown packet ID received : $packetType"
    )

    constructor(packet: Packet) : this(
            "Couldn't determine the ID for the packet : " + packet.javaClass.simpleName + ", is it declared in a .packets file ?"
    )

    companion object {

        private val serialVersionUID = 7612121415158158595L
    }

}
