//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.mods

import xyz.chunkstories.api.content.Asset
import xyz.chunkstories.api.exceptions.content.mods.NotAllModsLoadedException

/** The ModsManager is responsible for loading mods and maintaining a global filesystem of assets  */
interface ModsManager {
    // val enabledModsString: Array<String>

    val currentlyLoadedMods: Collection<Mod>

    // val allUniqueEntries: Collection<AssetHierarchy>

    val allAssets: Collection<Asset>
    // fun setEnabledMods(vararg modsEnabled: String)

    //@Throws(NotAllModsLoadedException::class)
    //fun loadEnabledMods()

    // fun getAllAssetsByExtension(extension: String): Collection<Asset>

    fun getAllAssetsByPrefix(prefix: String): Collection<Asset>

    fun getAsset(assetName: String): Asset?

    fun getAssetInstances(assetName: String): AssetHierarchy?

    fun getClassByName(className: String): Class<*>?
}