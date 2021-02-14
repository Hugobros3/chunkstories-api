//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.content.json.Json
import java.io.IOException

interface TraitSerializable {
    val traitName: String

    @Throws(IOException::class)
    fun serialize() : Json

    @Throws(IOException::class)
    fun deserialize(json: Json)
}
