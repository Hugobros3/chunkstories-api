//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.player

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.plugin.commands.CommandEmitter
import xyz.chunkstories.api.world.World

/** Represents a player (Remote or Local) as the once calling shots  */
interface Player : CommandEmitter, Controller, Subscriber, Interlocutor {
    /** @return the username of the player
     */
    override val name: String

    /** The displayable name of the player (including things like tags,
     * color etc)*/
    val displayName: String

    /** @return True once the player connection was interrupted
     */
    //TODO asses usage and remove/clean
    @Deprecated("unused?")
    val isConnected: Boolean

    /** @return True once the player has been spawned inside it's [World].
     */
    //TODO asses usage and remove/clean
    @Deprecated("unused?")
    fun hasSpawned(): Boolean

    /** @return the entity this player is controlling
     */
    override var controlledEntity: Entity?

    /** Sends a text message to this player chat  */
    override fun sendMessage(msg: String)

    /** Helper method: Tries to open the specified inventory for the following player  */
    fun openInventory(inventory: Inventory)
}
