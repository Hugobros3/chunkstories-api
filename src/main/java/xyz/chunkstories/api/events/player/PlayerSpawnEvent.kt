//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster

/** Player (re) spawn event - Triggered when a player is spawned into a world  */
class PlayerSpawnEvent(
        // Specific event code

        val player: Player, val world: WorldMaster,
        /** By default the entity is loaded from the players/[username].csf file if it
         * exists, else it's null. If no entity is set by a third-party plugin, a
         * default one will be provided  */
        /** Sets the entity to spawn the player as  */
        var entity: Entity?, var spawnLocation: Location?) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this
        var listenersStatic = EventListeners(PlayerSpawnEvent::class.java)
            internal set
    }

}
