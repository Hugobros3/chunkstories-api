//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world;

import xyz.chunkstories.api.net.RemoteServer;

/** Describes a World Client connected to a remote server */
public interface WorldClientNetworkedRemote extends WorldNetworked {

	public RemoteServer getRemoteServer();
}
