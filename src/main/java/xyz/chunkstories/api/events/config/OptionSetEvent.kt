//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.config

import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.EventListeners
import xyz.chunkstories.api.events.world.WorldTickEvent
import xyz.chunkstories.api.util.configuration.Configuration.Option

class OptionSetEvent(val option: Option<*>) : CancellableEvent() {

    override val listeners: EventListeners
        get() = listenersStatic

    companion object {

        var listenersStatic = EventListeners(WorldTickEvent::class.java)
            internal set
    }

}
