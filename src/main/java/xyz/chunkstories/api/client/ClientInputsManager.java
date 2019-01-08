//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client;

import xyz.chunkstories.api.input.Input;
import xyz.chunkstories.api.input.InputsManager;
import xyz.chunkstories.api.input.Mouse;

public interface ClientInputsManager extends InputsManager {
	public boolean onInputPressed(Input input);

	public boolean onInputReleased(Input input);

	public Mouse getMouse();
}
