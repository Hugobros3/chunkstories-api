//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content.mods

import xyz.chunkstories.api.content.mods.Mod

class ConflictingExternalCodeException(mod: Mod, message: String) : ModLoadFailureException(mod, message) {
    companion object {

        /**
         *
         */
        private val serialVersionUID = -6933179574010715068L
    }

}
