package xyz.chunkstories.api.events.player

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster

class PlayerLoginEvent(val player: Player) : CancellableEvent() {
    var connectionMessage: String? = "#FFFF00$player joined the server"
    var refusedConnectionMessage = "Connection was refused by a plugin."
}

class PlayerLogoutEvent(val player: Player) : Event() {
    var logoutMessage: String? = "#FFFF00$player left the server"
}

class PlayerChatEvent(val player: Player, val message: String) : CancellableEvent() {
    var formattedMessage: String = player.displayName + " > " + message
}

/**
 * Triggered when a player is switching to being ingame and controlling an entity
 * By default the entity is loaded from the players/[username].csf file if it
 * exists, else it's null. If no entity is set by a third-party plugin, a
 * default one will be provided
 */
class PlayerSpawnEvent(val player: Player, val world: WorldMaster, var entity: Entity?, var spawnLocation: Location?) : CancellableEvent()