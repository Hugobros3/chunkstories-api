//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.server.Host

/** A 'master' world is one hosting the game logic and who runs the 'serverside'
 * plugins. It can be either a dedicated server or a singleplayer world.  */
interface WorldMaster : World {
    val host: Host

    /** Returns the folder where the world files are on disk.  */
    val folderPath: String
}
