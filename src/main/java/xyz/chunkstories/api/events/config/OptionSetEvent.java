//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.config;

import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.events.world.WorldTickEvent;
import xyz.chunkstories.api.util.configuration.Configuration.Option;

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
