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

    /** @return the displayable name of the player (including things like tags,
     * color etc)
     */
    val displayName: String

    /** @return The [World] the entity belongs to.
     */
    val world: World

    /** @return The [context][GameContext] the world, and thus the entity
     * belongs to.
     */
    val context: GameContext

    /** @return The [Location] of this player in his world
     */
    /** Sets the [Location] of the user. Warning, can't change the world he's
     * in with this method !  */
    var location: Location

    /** @return True once the player connection was interrupted
     */
    val isConnected: Boolean

    /** @return the entity this player is controlling
     */
    override var controlledEntity: Entity?

    /** Sends a text message to this player chat  */
    override fun sendMessage(msg: String)

    /** @return True once the player has been spawned inside it's [World].
     */
    fun hasSpawned(): Boolean

    /** Helper method: Tries to open the specified inventory for the following
     * player  */
    fun openInventory(inventory: Inventory)
}
