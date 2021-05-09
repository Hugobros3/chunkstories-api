//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.workers;

/** An interface to submit tasks that will be processed in the background. */
public interface Tasks {

	/** Schedules the providen task to be executed. Use the task cancel() method to
	 * cancel the execution */
	public void scheduleTask(Task task);

	/** Returns how many tasks are already in the pipeline */
	public int submittedTasks();
}
