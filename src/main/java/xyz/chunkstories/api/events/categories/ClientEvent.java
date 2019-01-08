//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.categories;

import xyz.chunkstories.api.client.Client;

/** Describes events occuring client-side */
public interface ClientEvent {
	public Client getClient();
}
