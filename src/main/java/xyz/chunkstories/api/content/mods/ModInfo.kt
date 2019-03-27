//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.mods

/** Loads from mod.txt in the mod root directory  */
data class ModInfo(
        /** Get unique mod name  */
        val internalName: String,
        /** Get human-readable mod name  */
        val name: String,
        val version: String = "1.0",
        val description: String = "No description provided"
)