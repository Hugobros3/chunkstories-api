//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions

import java.io.DataInputStream

class NullItemException(internal var stream: DataInputStream) : ItemException() {

    override val message: String?
        get() {
        return "(Notice) Read a null ItemPile (ItemId=0) from stream $stream"
    }

    companion object {

        /**
         *
         */
        private val serialVersionUID = -8788184589175791958L
    }
}
