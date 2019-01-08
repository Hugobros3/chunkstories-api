//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events;

public interface EventExecutor {
	public void fireEvent(Event event) throws Exception;
}
