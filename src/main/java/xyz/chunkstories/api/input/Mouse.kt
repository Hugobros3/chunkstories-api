//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input

interface Mouse {
    val mainButton: MouseButton

    /** Returns true if the secondary mouse button is pressed  */
    val secondaryButton: MouseButton

    /** Returns true if the middle mouse button is pressed  */
    val middleButton: MouseButton

    val cursorX: Double

    val cursorY: Double

    var isGrabbed: Boolean

    interface MouseButton : ClientInput {
        val mouse: Mouse
    }

    /** Sent when the mouse scrolled (up or down)  */
    interface MouseScroll : ClientInput {
        fun amount(): Int
    }

    fun setMouseCursorLocation(x: Double, y: Double)
}
