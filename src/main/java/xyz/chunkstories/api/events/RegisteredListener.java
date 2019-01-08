//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events;

import xyz.chunkstories.api.plugin.ChunkStoriesPlugin;

/** Describes a successfully processed event handler annotation, called when a
 * specific event happens */
public class RegisteredListener {
	Listener listener;
	ChunkStoriesPlugin plugin;
	EventExecutor executor;
	EventHandler.EventPriority priority;

	public RegisteredListener(Listener listener, ChunkStoriesPlugin plugin, EventExecutor executor, EventHandler.EventPriority priority) {
		this.listener = listener;
		this.plugin = plugin;
		this.executor = executor;
		this.priority = priority;
	}

	public void invokeForEvent(Event event) throws Exception {
		executor.fireEvent(event);
	}
}
