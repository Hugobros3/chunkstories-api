//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events

import xyz.chunkstories.api.plugin.ChunkStoriesPlugin

/** Describes a successfully processed event handler annotation, called when a
 * specific event happens  */
class RegisteredListener(internal var listener: Listener, internal var plugin: ChunkStoriesPlugin, internal var executor: EventExecutor, internal var priority: EventHandler.EventPriority) {

    @Throws(Exception::class)
    fun invokeForEvent(event: Event) {
        executor.fireEvent(event)
    }
}
