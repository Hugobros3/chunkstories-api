//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.content.OnlineContentTranslator;

public interface WorldNetworked extends World
{	
	/** Returns a ContentTranslator that can deal with serializing live data */
	public OnlineContentTranslator getContentTranslator();
}
