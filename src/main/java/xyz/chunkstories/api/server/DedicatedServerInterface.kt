//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server

interface DedicatedServerInterface : Host {

    /** Stops the server.  */
    fun stop()
}
