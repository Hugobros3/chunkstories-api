//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.world;

import io.xol.chunkstories.api.events.Event;
import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.world.World;

public class WorldTickEvent extends Event
{
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(WorldTickEvent.class);

	@Override
	public EventListeners getListeners()
	{
		return listeners;
	}

	public static EventListeners getListenersStatic()
	{
		return listeners;
	}

	// Specific event code

	private World world;

	public WorldTickEvent(World world)
	{
		this.world = world;
	}

	public World getWorld()
	{
		return world;
	}
}
