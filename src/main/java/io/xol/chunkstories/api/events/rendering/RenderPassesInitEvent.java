package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.rendering.pass.RenderPasses;

public class RenderPassesInitEvent extends Event {

	static EventListeners listeners = new EventListeners(RenderPassesInitEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}
	
	final RenderPasses passes;

	public RenderPassesInitEvent(RenderPasses pipeline) {
		super();
		this.passes = pipeline;
	}

	public RenderPasses getPasses() {
		return passes;
	}
}
