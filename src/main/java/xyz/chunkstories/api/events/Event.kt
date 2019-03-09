//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events

abstract class Event {
    abstract val listeners: EventListeners

    /** Executed when the event has been passed to all listening plugins. May check
     * if event was canceled if the implementation allows it  */
    // public abstract void defaultBehaviour();
}
