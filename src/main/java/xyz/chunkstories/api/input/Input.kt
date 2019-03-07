//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input

/** Describe any form of input (keyboard, mouse, controller(planned),
 * virtual(servers or internal))  */
interface Input {
    /** Returns the name of the bind  */
    val name: String

    /** Returns true if the key is pressed  */
    val isPressed: Boolean

    /** Returns an unique identifier so server and client can communicate their
     * inputs  */
    val hash: Long

    /** Returns false if null Returns true if o is an input and
     * o.getRegisteredComponentName().equals(getRegisteredComponentName()) Returns
     * true if o is a String and o.equals(getRegisteredComponentName()) Returns
     * false otherwise.  */
    override fun equals(o: Any?): Boolean
}
