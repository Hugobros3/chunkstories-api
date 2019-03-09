//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.voxel

import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.world.cell.CellData

open class VoxelModificationEvent(context: CellData, newData: CellData,
        // Specific event code

                                  val modificationCause: WorldModificationCause) : VoxelEvent(context) {

    override val listeners: EventListeners
        get() = listenersStatic
    var newData: CellData
        internal set

    val modification: ModifiationType
        get() = if (context.voxel!!.isAir())
            ModifiationType.PLACEMENT
        else {
            if (newData.voxel!!.isAir())
                ModifiationType.DESTRUCTION
            else
                ModifiationType.REPLACEMENT
        }

    init {
        this.newData = newData
    }

    enum class ModifiationType {
        DESTRUCTION, PLACEMENT, REPLACEMENT
    }

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(VoxelModificationEvent::class.java)
            internal set
    }
}
