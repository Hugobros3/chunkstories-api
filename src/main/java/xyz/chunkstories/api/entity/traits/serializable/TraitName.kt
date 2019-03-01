//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

class TraitName(entity: Entity) : TraitSerializable(entity) {

    var name = ""
        set(value) {
            field = value
            this.pushComponentEveryone()
        }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeUTF(name)
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        name = dis.readUTF()
    }

}
