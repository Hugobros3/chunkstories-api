//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content.mods

import xyz.chunkstories.api.content.mods.Mod

open class ModLoadFailureException(internal var mod: Mod, val modLoadError: String) : Exception() {

    override val message: String?
        get() = "Mod '$mod' failed to load : $modLoadError"

    companion object {
        private val serialVersionUID = -3181028531069214061L
    }
}
