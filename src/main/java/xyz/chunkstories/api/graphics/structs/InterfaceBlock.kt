//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.structs

/** This interface is the bridge between the Java/Kotlin world and the GLSL world.
 * Any class implementing this interface can, if some rules are respected, be seamlessly
 * translated to a GLSL data structure and used as-is in the shader code.
 *
 * This is useful for interfacing stuff like UBOs and even supplying per-instance model data.*/
interface InterfaceBlock

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UpdateFrequency(val frequency: UniformUpdateFrequency)

enum class UniformUpdateFrequency {
    /** Information that is specific to the object or batch of objects for the next draw. Consider using per-instance data instead */
    ONCE_PER_BATCH,
    /** Information a system needs to do it's job properly ( image inputs from previous passes for instance )*/
    ONCE_PER_SYSTEM,
    /** Information that is constant accross a render task, typically camera information*/
    ONCE_PER_RENDER_TASK,
    /** Information that is global to the frame, such as the frame number, world time, animation time etc. */
    ONCE_PER_FRAME
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class IgnoreGLSL