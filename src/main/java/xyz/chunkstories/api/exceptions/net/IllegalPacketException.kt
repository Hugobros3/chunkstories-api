//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.net

import xyz.chunkstories.api.net.Packet

class IllegalPacketException(internal var packet: Packet) : Exception() {

    override val message: String
        get() {
        return "Illegal packet received : " + packet.javaClass.name + ""
    }

    companion object {

        /**
         *
         */
        private val serialVersionUID = 4148448942644331785L
    }

}
