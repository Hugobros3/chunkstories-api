//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content.mods;

import java.util.Iterator;

import io.xol.chunkstories.api.content.Asset;

/**
 * Some assets may get overloaded by mods, and in some cases you still want to
 * read all versions, this interface allows just that
 */
public interface AssetHierarchy extends Iterable<Asset> {
	/** Returns the name of the asset overloaded */
	public String getName();

	/**
	 * Returns the "top" asset ( the one the ModsManager returns if you ask for it
	 * by name
	 */
	public Asset topInstance();

	/** Returns an iterator from higher to lower priority */
	public Iterator<Asset> iterator();
}