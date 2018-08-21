//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util.concurrency;

/** A fence can stop the program's execution until a condition is met. */
public interface Fence {
	public void traverse();
}
