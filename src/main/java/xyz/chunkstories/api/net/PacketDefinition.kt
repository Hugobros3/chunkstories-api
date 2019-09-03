//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net

import xyz.chunkstories.api.content.Definition

interface PacketDefinition {
    val name: String

    val genre: PacketGenre

    val allowedFrom: AllowedFrom

    enum class AllowedFrom {
        ALL, CLIENT, SERVER
    }

    /** There are only 4 genders */
    enum class PacketGenre {
        /** Internal packets the engine uses to negotiate connections and do it's job */
        SYSTEM,
        /** For miscellaneous uses, both the engine and mods may use those  */
        GENERAL_PURPOSE,
        /**
         * Tied to the world ticks; contains a reference to the particular world;
         * interpreted during the world tick function
         */
        WORLD,
        /**
         * Tied to the world data streaming; contains a reference to the particular
         * world;
         */
        WORLD_STREAMING
    }
}