//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.tasks

import xyz.chunkstories.api.workers.Task

/** Thrown when we were waiting on a task that gets cancelled  */
class UnexecutableTaskException @JvmOverloads constructor(internal val task: Task, reason: String? = null) : RuntimeException("Task: " + task + " isn't executable on this taskInstance" + if (reason != null) " Reason: $reason" else "") {
    companion object {

        private val serialVersionUID = -8120446386095147517L
    }
}
