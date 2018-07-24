//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client;

import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.input.InputsManager;
import io.xol.chunkstories.api.input.Mouse;

public interface ClientInputsManager extends InputsManager {
	public boolean onInputPressed(Input input);

	public boolean onInputReleased(Input input);

	public Mouse getMouse();
}
