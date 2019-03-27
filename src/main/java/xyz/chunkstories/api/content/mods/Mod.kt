//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.mods

import xyz.chunkstories.api.content.Asset
import xyz.chunkstories.api.util.IterableIterator

/** A mod contains assets that add to or override the game's defaults.  */
interface Mod {
    val modInfo: ModInfo

    val mD5Hash: String
    /** Returns the asset corresponding to the provided path, matching the syntax
     * ./directory/subdirectory/asset.txt Returns only the version defined in this
     * mod. Returns null if the asset couln't be found  */
    fun getAssetByName(name: String): Asset?

    /** Iterates over this mod's assets  */
    val assets: Collection<Asset>

}