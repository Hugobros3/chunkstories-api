package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.rendering.RenderingPipeline;

public class RenderingPipelineInitEvent extends Event {

	static EventListeners listeners = new EventListeners(RenderingPipelineInitEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}
	
	final RenderingPipeline pipeline;

	public RenderingPipelineInitEvent(RenderingPipeline pipeline) {
		super();
		this.pipeline = pipeline;
	}

	public RenderingPipeline getPipeline() {
		return pipeline;
	}
}
