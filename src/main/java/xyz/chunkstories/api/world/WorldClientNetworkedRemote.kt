//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.entity.Subscriber

/** Describes a World Client connected to a remote server  */
interface WorldClientNetworkedRemote : WorldClient {
    val remoteServer: Subscriber
}