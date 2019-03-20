//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world

import xyz.chunkstories.api.world.World

class RegionNotLoadedException(world: World, val regionX: Int, val regionY: Int, val regionZ: Int) : WorldException(world) {

    override val message: String?
        get() {
        return "Region at " + regionX + ":" + regionY + ":" + regionZ + "was not loaded"
    }

    companion object {

        private val serialVersionUID = -451869658162736185L
    }

}
