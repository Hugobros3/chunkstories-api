//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content.mods;

import io.xol.chunkstories.api.content.Asset;
import io.xol.chunkstories.api.util.IterableIterator;

import javax.annotation.Nullable;

/**
 * A mod contains assets that add to or override the game's defaults.
 */
public interface Mod {
	/**
	 * Returns the asset corresponding to the provided path, matching the syntax
	 * ./directory/subdirectory/asset.txt Returns only the version defined in this
	 * mod. Returns null if the asset couln't be found
	 */
	@Nullable
	public Asset getAssetByName(String name);

	/** Iterates over this mod's assets */
	public IterableIterator<Asset> assets();

	public ModInfo getModInfo();

	public String getMD5Hash();

}