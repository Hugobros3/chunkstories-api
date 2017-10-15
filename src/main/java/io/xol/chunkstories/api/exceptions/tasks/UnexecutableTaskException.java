package io.xol.chunkstories.api.exceptions.tasks;

import io.xol.chunkstories.api.workers.Task;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

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
