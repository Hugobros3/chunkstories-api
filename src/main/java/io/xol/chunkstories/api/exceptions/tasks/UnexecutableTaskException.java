//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.tasks;

import io.xol.chunkstories.api.workers.Task;

/** Thrown when we were waiting on a task that gets cancelled */
public class UnexecutableTaskException extends RuntimeException {

	final Task task;
	
	public UnexecutableTaskException(Task task) {
		this(task, null);
	}
	
	public UnexecutableTaskException(Task task, String reason) {
		super("Task: "+task + " isn't executable on this context" + (reason != null ? " Reason: " + reason : ""));
		this.task = task;
	}

	private static final long serialVersionUID = -8120446386095147517L;
}
