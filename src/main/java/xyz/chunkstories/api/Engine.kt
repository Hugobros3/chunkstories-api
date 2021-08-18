//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api

import org.slf4j.Logger
import xyz.chunkstories.api.workers.Tasks

/** Parent interface to Client/Server */
interface Engine {
    val tasks: Tasks
    val logger: Logger
}