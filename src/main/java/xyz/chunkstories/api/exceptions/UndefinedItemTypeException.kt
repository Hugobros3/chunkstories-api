//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions

class UndefinedItemTypeException(internal var itemId: Int) : ItemException() {

    override val message: String?
        get() {
        return "Unknown ItemType by id=$itemId"
    }

    companion object {

        /**
         *
         */
        private val serialVersionUID = 3629935518207497054L
    }

}
