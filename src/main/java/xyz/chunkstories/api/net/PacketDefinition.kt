//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net


interface PacketDefinition {
    val name: String
    val allowedFrom: AllowedFrom

    enum class AllowedFrom {
        ALL, CLIENT, SERVER
    }
}