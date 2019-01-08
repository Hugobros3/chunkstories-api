//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.client;

import xyz.chunkstories.api.client.Client;
import xyz.chunkstories.api.events.Event;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.input.Input;

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

	public ClientInputReleasedEvent(Client client, Input input) {
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
