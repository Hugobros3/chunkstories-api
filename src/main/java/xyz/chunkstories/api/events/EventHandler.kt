//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class EventHandler {
    @Deprecated("Unimplemented for now, maybe later?")
    enum class EventPriority {
        LOWEST, LOW, NORMAL, HIGH, HIGHER, HIGHEST
    }
}