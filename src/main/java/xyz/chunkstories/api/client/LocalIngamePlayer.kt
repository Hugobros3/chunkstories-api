//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import xyz.chunkstories.api.graphics.Window
import xyz.chunkstories.api.player.Player

interface LocalPlayer : Player {
    /** Returns the client playing  */
    val client: Client

    /** Returns access to the game window  */
    val window: Window

    /** @return Is the game GUI in focus or obstructed by other things ?
     */
    fun hasFocus(): Boolean
}