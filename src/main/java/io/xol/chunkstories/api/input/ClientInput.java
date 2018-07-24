//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.input;

import io.xol.chunkstories.api.client.ClientInterface;

/**
 * Clientside input, presses are replicated on the server but they use
 * VirtualInputs instead of these
 */
public interface ClientInput extends Input {
	public ClientInterface getClient();
}
