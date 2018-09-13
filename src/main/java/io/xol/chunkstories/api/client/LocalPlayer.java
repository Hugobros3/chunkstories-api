//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client;

import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.graphics.Window;

public interface LocalPlayer extends Player {
	/** Returns the client playing */
	public Client getClient();

	/** Gives access to the input subsystem */
	public ClientInputsManager getInputsManager();

	/** @return Is the game GUI in focus or obstructed by other things ? */
	public boolean hasFocus();

	/** Returns access to the game window */
	public Window getWindow();
}
