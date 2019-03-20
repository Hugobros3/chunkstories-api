//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world

import xyz.chunkstories.api.world.World

abstract class WorldException(val world: World) : Exception()
