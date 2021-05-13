//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.world

import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.world.World

class WorldTickEvent(val world: World) : Event()