//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin.commands

import xyz.chunkstories.api.server.Host

/** Represents the console of a standalone server */
interface ServerConsole : CommandEmitter {
    val server: Host
}
