//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world

import xyz.chunkstories.api.world.chunk.Chunk

abstract class ChunkException(val chunk: Chunk) : RegionException(chunk.region)
