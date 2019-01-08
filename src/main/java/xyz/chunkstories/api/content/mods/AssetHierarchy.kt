//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.mods

import xyz.chunkstories.api.content.Asset

/** Some assets may get overloaded by mods, and in some cases you still want to
 * read all versions, this interface allows just that  */
interface AssetHierarchy {
    /** Returns the name of the asset overloaded  */
    val name: String

    /** Returns the "top" asset ( the one the ModsManager returns if you ask for it
     * by name  */
    val topInstance: Asset

    /** Returns an iterator from higher to lower priority  */
    val instances: List<Asset>
}