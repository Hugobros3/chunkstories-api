//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content

class IllegalPacketDeclarationException(msg: String) : Exception(msg) {
    companion object {
        private val serialVersionUID = 3624708384047661361L
    }

}
