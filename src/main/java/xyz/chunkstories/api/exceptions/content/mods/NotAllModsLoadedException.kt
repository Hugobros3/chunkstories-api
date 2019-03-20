//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content.mods

class NotAllModsLoadedException(internal var failed: Collection<ModLoadFailureException>) : Exception() {

    override val message: String?
        get() {
            var message = "Some mods failed to load : \n"

            for (e in failed)
                message += e.message + "\n"

            return message
        }

    companion object {

        /**
         *
         */
        private val serialVersionUID = 5136184783162902334L
    }

}
