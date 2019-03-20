//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content.mods

class ModDownloadFailedException(internal var modName: String, val modLoadError: String) : Exception() {

    override val message: String?
        get() = "Mod '$modName' failed to load : $modLoadError"

    companion object {
        private val serialVersionUID = -8878214806405897338L
    }

}
