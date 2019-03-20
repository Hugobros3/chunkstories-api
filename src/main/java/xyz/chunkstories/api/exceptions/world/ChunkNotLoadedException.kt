//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world

import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.region.Region

class ChunkNotLoadedException(world: World, val region: Region?, val chunkX: Int, val chunkY: Int, val chunkZ: Int) : WorldException(world) {

    override val message: String?
        get() {
        return if (region == null)
            "Chunk at " + chunkX + ":" + chunkY + ":" + chunkZ + "was not loaded"
        else
            "Chunk at " + chunkX + ":" + chunkY + ":" + chunkZ + "was not loaded (but the underlying region was)"
    }

    companion object {

        private val serialVersionUID = -451869658162736185L
    }

}
