//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.mods;

import java.util.Collection;
import java.util.Iterator;

import xyz.chunkstories.api.content.Asset;
import xyz.chunkstories.api.exceptions.content.mods.NotAllModsLoadedException;

import javax.annotation.Nullable;

/** The ModsManager is responsible for loading mods and maintaining a global
 * filesystem of assets */
public interface ModsManager {
	public void setEnabledMods(String... modsEnabled);

	public String[] getEnabledModsString();

	public void loadEnabledMods() throws NotAllModsLoadedException;

	public Collection<Mod> getCurrentlyLoadedMods();

	public Collection<AssetHierarchy> getAllUniqueEntries();

	public Collection<Asset> getAllAssets();

	public Collection<Asset> getAllAssetsByExtension(String extension);

	public Collection<Asset> getAllAssetsByPrefix(String prefix);

	@Nullable
	public Asset getAsset(String assetName);

	@Nullable
	public AssetHierarchy getAssetInstances(String assetName);

	@Nullable
	public Class<?> getClassByName(String className);
}