//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions

class UnauthorizedClientActionException(internal var functionCalled: String) : RuntimeException() {

    override val message: String?
        get() {
        return "Illegal master function : $functionCalled got called but the world is not master."
    }

    companion object {
        private val serialVersionUID = 800139003416109519L
    }
}
