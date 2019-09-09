//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.net.packets.PacketEntity
import xyz.chunkstories.api.util.Generalized
import xyz.chunkstories.api.util.SerializedName
import xyz.chunkstories.api.world.serialization.OfflineSerializedData
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

interface TraitSerializable {
    val traitName: String

    @Throws(IOException::class)
    fun serialize() : Json

    @Throws(IOException::class)
    fun deserialize(json: Json)
}
