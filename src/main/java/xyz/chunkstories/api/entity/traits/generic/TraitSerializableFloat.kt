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

/** Generic class for not duplacting boring code everywhere Remember: you still
 * have to declare the actual components classes in .components files !  */
@Generalized
abstract class TraitSerializableFloat(entity: Entity, private var value: Float) : TraitSerializable(entity) {

    fun getValue(): Float {
        return value
    }

    fun setValue(newValue: Float): Boolean {
        this.value = newValue

        this.pushComponentEveryone()
        return true
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeFloat(value)
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        value = dis.readFloat()
    }

}
