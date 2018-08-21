package io.xol.chunkstories.api.graphics.structs

/** This interface is the bridge between the Java/Kotlin world and the GLSL world.
 * Any class implementing this interface can, if some rules are respected, be seamlessly
 * translated to a GLSL data structure and used as-is in the shader code.
 *
 * This is useful for interfacing stuff like UBOs and even supplying per-instance model data.*/
interface InterfaceBlock