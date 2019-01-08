//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input;

import xyz.chunkstories.api.client.Client;

/** Clientside input, presses are replicated on the server but they use
 * VirtualInputs instead of these */
public interface ClientInput extends Input {
	public Client getClient();
}
