//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.entity.Subscriber;

/**
 * The remote server interface is used as a virtual subscriber ( the server being the 'subscriber' ) to the controlled entity of the client
 * so it receives updates from the client.
 */
public interface RemoteServer extends Subscriber, Interlocutor
{

}
