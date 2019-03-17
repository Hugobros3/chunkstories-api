//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.generic

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.serializable.TraitSerializable
import xyz.chunkstories.api.util.Generalized
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

@Generalized
abstract class TraitSerializableBoolean(entity: Entity) : TraitSerializable(entity) {
    private var value = false

    fun get(): Boolean {
        return value
    }

    fun set(newValue: Boolean) {
        if (this.value != newValue) {
            this.value = newValue
            this.pushComponentEveryone()
        }
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeBoolean(value)
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        value = dis.readBoolean()
    }

}
