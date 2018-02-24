//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.tasks;

import io.xol.chunkstories.api.workers.Task;

/** Thrown when we were waiting on a task that gets cancelled */
public class CancelledTaskException extends RuntimeException {

	final Task task;
	
	public CancelledTaskException(Task task) {
		super("Was waiting on task: "+task + " but it got cancelled.");
		this.task = task;
	}

	private static final long serialVersionUID = 5157397465657644674L;
}
