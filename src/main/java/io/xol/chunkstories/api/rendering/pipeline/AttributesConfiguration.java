//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.pipeline;

import java.util.Map;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.vertex.AttributeSource;

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
	
	/**
	 * Used by RenderingCommands to determine if they can be merged together and instanced
	 */
	public boolean isCompatibleWith(AttributesConfiguration attributesConfiguration);

	public void setup(RenderingInterface renderingInterface);
}
