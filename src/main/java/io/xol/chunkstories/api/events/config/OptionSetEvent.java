//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.config;

import io.xol.chunkstories.api.events.CancellableEvent;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.events.world.WorldTickEvent;
import io.xol.chunkstories.api.util.configuration.Configuration.Option;

public class OptionSetEvent extends CancellableEvent {

	static EventListeners listeners = new EventListeners(WorldTickEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	final Option option;

	public Option getOption() {
		return option;
	}

	public OptionSetEvent(Option option) {
		this.option = option;
	}

}
