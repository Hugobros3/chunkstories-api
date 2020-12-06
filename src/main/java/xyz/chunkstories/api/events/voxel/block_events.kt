//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.voxel

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.player.IngamePlayer
import xyz.chunkstories.api.voxel.MiningTool
import xyz.chunkstories.api.world.WorldCell

data class PlayerMineBlockEvent(val player: IngamePlayer, val cell: WorldCell, val tool: MiningTool) : CancellableEvent()