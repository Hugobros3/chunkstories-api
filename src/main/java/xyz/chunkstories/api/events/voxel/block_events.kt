//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.voxel

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.block.MiningTool
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldCell
import xyz.chunkstories.api.world.cell.MutableCellData

data class PlayerMineBlockEvent(val player: Player, val cell: WorldCell, val tool: MiningTool) : CancellableEvent()
data class PlayerPlaceBlockEvent(val player: Player, val cell: WorldCell, val newData: MutableCellData) : CancellableEvent()