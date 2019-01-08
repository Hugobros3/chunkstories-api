//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input;

import java.util.Iterator;

public interface InputsManager {
	public Input getInputByName(String inputName);

	public Input getInputFromHash(long hash);

	public Iterator<Input> getAllInputs();
}
