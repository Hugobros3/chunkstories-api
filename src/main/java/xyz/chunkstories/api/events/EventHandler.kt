//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class EventHandler(
        /** Good ol' Bukkit-style priority handling  */
        val priority: EventPriority = EventPriority.NORMAL,
        /**
         * Should we listen for child events ( extended classes of the event ) ? Example
         * use case: a voxel protection plugin that bans touching a location by any
         * means with a single event, unlike what we used to have to do
         */
        val listenToChildEvents: ListenToChildEvents = ListenToChildEvents.NO) {

    enum class EventPriority {
        LOWEST, LOW, NORMAL, HIGH, HIGHER, HIGHEST
    }

    enum class ListenToChildEvents {
        /** Only care about this very event  */
        NO,
        /** Only do for 1st class extended classes  */
        YES,
        /** Care about every single child event class  */
        RECURSIVE
    }
}