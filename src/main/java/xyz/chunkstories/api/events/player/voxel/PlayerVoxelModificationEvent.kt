//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player.voxel

import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.events.voxel.VoxelModificationEvent
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.cell.CellData

class PlayerVoxelModificationEvent(context: CellData, newData: CellData, cause: WorldModificationCause, // Specific event code
                                   val player: Player) : VoxelModificationEvent(context, newData, cause) {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(PlayerVoxelModificationEvent::class.java)
            internal set
    }
}
