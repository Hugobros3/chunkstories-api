//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input

/** Describes a key assignated to some action  */
interface KeyboardKeyInput : Input {
    /** Returns the name of the bind  */
    override val name: String

    /** Returns true if the key is pressed  */
    override val isPressed: Boolean
}
