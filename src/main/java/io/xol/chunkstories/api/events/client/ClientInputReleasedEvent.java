//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.client;

import io.xol.chunkstories.api.client.ClientInterface;
import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.input.Input;

/** Called when the client releases an input of some sort. */
public class ClientInputReleasedEvent extends Event {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(ClientInputReleasedEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	public ClientInputReleasedEvent(ClientInterface client, Input input) {
		this.client = client;
		this.input = input;
	}

	private final ClientInterface client;
	private final Input input;

	public ClientInterface getClient() {
		return client;
	}

	public Input getInput() {
		return input;
	}
}
