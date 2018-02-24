//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.rendering;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.world.WorldRenderer;
import io.xol.chunkstories.api.world.World;

/** Called upon entering some rendering states */
public abstract class WorldRenderingEvent extends Event
{
	private final World world;
	private final WorldRenderer worldRenderer;
	private final RenderingInterface renderingInterface;
	
	public WorldRenderingEvent(World world, WorldRenderer worldRenderer, RenderingInterface renderingInterface)
	{
		super();
		this.world = world;
		this.worldRenderer = worldRenderer;
		this.renderingInterface = renderingInterface;
	}
	
	public World getWorld()
	{
		return world;
	}

	public WorldRenderer getWorldRenderer()
	{
		return worldRenderer;
	}

	public RenderingInterface getRenderingInterface()
	{
		return renderingInterface;
	}
}
