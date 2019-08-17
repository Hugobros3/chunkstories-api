//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.ai

import xyz.chunkstories.api.entity.Entity

abstract class AI<E : Entity>(val entity: E) {
    var currentTask: AiTask<E> = AiTaskIdle(this)

    open fun tick() {
        currentTask.execute()
    }
}

/** Using inner classes for those was a mistake */
abstract class AiTask<E : Entity>(val ai: AI<E>) {
    val entity: E
        get() = ai.entity

    abstract fun execute()
}

class AiTaskIdle<E: Entity>(ai: AI<E>) : AiTask<E>(ai) {
    override fun execute() {
        // Do nothing (idle)
    }
}