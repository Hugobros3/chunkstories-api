package io.xol.chunkstories.api.content.mods;

import io.xol.chunkstories.api.content.Definition;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

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