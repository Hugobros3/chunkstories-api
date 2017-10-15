package io.xol.chunkstories.api.exceptions.tasks;

import io.xol.chunkstories.api.workers.Task;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Thrown when we were waiting on a task that gets cancelled */
public class CancelledTaskException extends RuntimeException {

	final Task task;
	
	public CancelledTaskException(Task task) {
		super("Was waiting on task: "+task + " but it got cancelled.");
		this.task = task;
	}

	private static final long serialVersionUID = 5157397465657644674L;
}
