//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content.mods;

import io.xol.chunkstories.api.content.Definition;

/**
 * Loads from mod.txt in the mod root directory
 */
public interface ModInfo extends Definition
{
	public Mod getMod();

	/** Get unique mod name */
	public String getInternalName();
	
	/** Get human-readable mod name */
	public String getName();

	public String getVersion();

	public String getDescription();
}