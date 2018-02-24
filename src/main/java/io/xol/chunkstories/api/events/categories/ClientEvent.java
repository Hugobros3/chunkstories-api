//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.categories;

import io.xol.chunkstories.api.client.ClientInterface;

/**
 * Describes events occuring client-side
 */
public interface ClientEvent
{
	public ClientInterface getClient();
}
