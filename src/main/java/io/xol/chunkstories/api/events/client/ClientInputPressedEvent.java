//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.client;

import io.xol.chunkstories.api.client.Client;
import io.xol.chunkstories.api.events.CancellableEvent;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.input.Input;

/** Called when the client presses an input of some sort. */
public class ClientInputPressedEvent extends CancellableEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(ClientInputPressedEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	public ClientInputPressedEvent(Client client, Input input) {
		this.client = client;
		this.input = input;
	}

	private final Client client;
	private final Input input;

	public Client getClient() {
		return client;
	}

	public Input getInput() {
		return input;
	}
}
