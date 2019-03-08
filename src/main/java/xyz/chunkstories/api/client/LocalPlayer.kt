//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.graphics.Window

interface LocalPlayer : Player {
    /** Returns the client playing  */
    val client: Client

    /** Gives access to the input subsystem  */
    override val inputsManager: ClientInputsManager

    /** Returns access to the game window  */
    val window: Window

    /** @return Is the game GUI in focus or obstructed by other things ?
     */
    fun hasFocus(): Boolean
}
