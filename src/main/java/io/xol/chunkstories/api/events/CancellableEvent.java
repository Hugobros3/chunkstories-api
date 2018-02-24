//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events;

public abstract class CancellableEvent extends Event {
	
	private boolean canceled = false;

	public void setCancelled(boolean canceled)
	{
		this.canceled  = canceled;
	}
	
	public boolean isCancelled()
	{
		return canceled;
	}
}
