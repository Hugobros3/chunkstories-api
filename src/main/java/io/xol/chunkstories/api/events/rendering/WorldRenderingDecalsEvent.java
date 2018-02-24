//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.effects.DecalsRenderer;
import io.xol.chunkstories.api.rendering.world.WorldRenderer;
import io.xol.chunkstories.api.world.World;

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
