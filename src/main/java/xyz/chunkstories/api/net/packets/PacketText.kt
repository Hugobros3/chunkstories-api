//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.Engine
import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.server.UserConnection
import java.io.DataInputStream
import java.io.DataOutputStream

open class PacketText(engine: Engine) : Packet(engine) {
    lateinit var text: String

    constructor(engine: Engine, text: String) : this(engine) {
        this.text = text
    }

    override fun send(dos: DataOutputStream) {
        dos.writeUTF(text)
    }

    override fun receive(dis: DataInputStream, user: UserConnection?) {
        text = dis.readUTF()
    }
}