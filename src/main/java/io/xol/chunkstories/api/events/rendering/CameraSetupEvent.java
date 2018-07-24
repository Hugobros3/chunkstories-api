//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.rendering.CameraInterface;

public class CameraSetupEvent extends Event {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(CameraSetupEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	private CameraInterface camera;

	public CameraSetupEvent(CameraInterface camera) {
		this.camera = camera;
	}

	public CameraInterface getCamera() {
		return camera;
	}
}
