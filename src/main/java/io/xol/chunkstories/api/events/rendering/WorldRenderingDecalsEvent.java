package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.WorldRenderer;
import io.xol.chunkstories.api.rendering.effects.DecalsRenderer;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Called during the decals rendering pass, after the default renderer is done
 * rendering the normal decals
 */
public class WorldRenderingDecalsEvent extends WorldRenderingEvent {

	static EventListeners listeners = new EventListeners(WorldRenderingDecalsEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	public WorldRenderingDecalsEvent(World world, WorldRenderer worldRenderer, RenderingInterface renderingInterface,
			DecalsRenderer decalsRenderer) {
		super(world, worldRenderer, renderingInterface);

		this.decalsRenderer = decalsRenderer;
	}

	private final DecalsRenderer decalsRenderer;

	public DecalsRenderer getDecalsRenderer() {
		return decalsRenderer;
	}
}
