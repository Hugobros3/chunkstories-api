//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.input;

/**
 * Describes a key assignated to some action
 */
public interface KeyboardKeyInput extends Input {
	/**
	 * Returns the name of the bind
	 */
	@Override
	public String getName();

	/**
	 * Returns true if the key is pressed
	 */
	@Override
	public boolean isPressed();
}
