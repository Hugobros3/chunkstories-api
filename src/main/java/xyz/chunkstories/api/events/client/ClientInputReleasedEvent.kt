//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.client

import xyz.chunkstories.api.client.Client
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.input.Input

/** Called when the client releases an input of some sort.  */
class ClientInputReleasedEvent
// Specific event code

(val client: Client, val input: Input) : Event() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {
        // Every event class has to have this

        var listenersStatic = EventListeners(ClientInputReleasedEvent::class.java)
            internal set
    }
}
