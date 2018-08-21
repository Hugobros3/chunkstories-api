//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content.mods;

import java.util.Collection;
import java.util.Iterator;

import io.xol.chunkstories.api.content.Asset;
import io.xol.chunkstories.api.exceptions.content.mods.NotAllModsLoadedException;

import javax.annotation.Nullable;

/** The ModsManager is responsible for loading mods and maintaining a global
 * filesystem of assets */
public interface ModsManager {
	public void setEnabledMods(String... modsEnabled);

	public void loadEnabledMods() throws NotAllModsLoadedException;

	public Iterator<AssetHierarchy> getAllUniqueEntries();

	public Iterator<Asset> getAllUniqueFilesLocations();

	@Nullable
	public Asset getAsset(String assetName);

	@Nullable
	public AssetHierarchy getAssetInstances(String assetName);

	public Iterator<Asset> getAllAssetsByExtension(String extension);

	public Iterator<Asset> getAllAssetsByPrefix(String prefix);

	@Nullable
	public Class<?> getClassByName(String className);

	public String[] getEnabledModsString();

	public Collection<Mod> getCurrentlyLoadedMods();
}