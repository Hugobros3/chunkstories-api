//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.categories

import xyz.chunkstories.api.player.Player

/** Describes an event triggered, centered arround or related to a player.
 *
 * @author Hugo
 */
interface PlayerEvent {
    /** Returns the player affected by the event. If two or more players are
     * concerned, only the 'main' one will be returned.
     *
     * @return
     */
    val player: Player
}
