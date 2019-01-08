//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world;

import xyz.chunkstories.api.content.OnlineContentTranslator;

public interface WorldNetworked extends World {
	/** Returns a ContentTranslator that can deal with serializing live data */
	public OnlineContentTranslator getContentTranslator();
}
