//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.vertex;

import java.util.Map;

/**
 * Abstracts the vertex shader inputs
 */
public interface AttributesConfiguration
{
	/**
	 * Returns the currently bound attributes
	 * @return
	 */
	public Map<String, AttributeSource> getBoundAttributes();
}
