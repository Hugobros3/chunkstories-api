//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.voxel

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.world.cell.Cell

abstract class VoxelEvent(
        // Specific event code

        /** Returns the taskInstance before the voxel destruction  */
        val context: Cell) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(VoxelEvent::class.java)
            internal set
    }
}
