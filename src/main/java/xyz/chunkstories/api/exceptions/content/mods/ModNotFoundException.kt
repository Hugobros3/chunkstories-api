//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content.mods

import java.lang.Exception

class ModNotFoundException(internal var modName: String) : Exception() {

    override val message: String?
        get() = "Mod '$modName' was not found."

    companion object {

        /**
         *
         */
        private val serialVersionUID = -5671040280199985929L
    }

}
