//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events;

public abstract class Event {
	public abstract EventListeners getListeners();

	/** Executed when the event has been passed to all listening plugins. May check
	 * if event was canceled if the implementation allows it */
	// public abstract void defaultBehaviour();
}
