//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.vertex;

/**
 * A data source for vertex shaders attribute inputs
 */
public interface AttributeSource {
	/**
	 * Setups this attributeSource to it's slot, the enabling/allocation/disabling
	 * of vertex attributes is up to the engine This isn't really meant to be a part
	 * of the specification, but is for simplicity reasons. Sorry for
	 * platform-agnosticity fans :/
	 */
	public void setup(int gl_AttributeLocation);
}
