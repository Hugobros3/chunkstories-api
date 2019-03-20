//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.tasks

import xyz.chunkstories.api.workers.Task

/** Thrown when we were waiting on a task that gets cancelled  */
class CancelledTaskException(internal val task: Task) : RuntimeException("Was waiting on task: $task but it got cancelled.") {
    companion object {

        private val serialVersionUID = 5157397465657644674L
    }
}
