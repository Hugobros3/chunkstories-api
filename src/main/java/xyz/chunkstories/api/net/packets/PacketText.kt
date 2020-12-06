//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.player.Player
import java.io.DataInputStream
import java.io.DataOutputStream

abstract class PacketText : Packet {
    lateinit var text: String

    constructor()
    constructor(text: String) {
        this.text = text
    }

    override fun send(dos: DataOutputStream) {
        dos.writeUTF(text)
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        text = dis.readUTF()
    }
}