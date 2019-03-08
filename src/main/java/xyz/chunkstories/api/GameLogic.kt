//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api

import xyz.chunkstories.api.plugin.PluginManager
import xyz.chunkstories.api.plugin.Scheduler

interface GameLogic {
    val gameContext: GameContext

    val targetFps: Int

    val simulationFps: Double

    /** Default: 1x */
    val simulationSpeed: Double

    val pluginsManager: PluginManager

    val scheduler: Scheduler
}