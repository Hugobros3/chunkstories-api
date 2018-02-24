//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

public class ModNotFoundException extends ModLoadFailureException
{
	String modName;
	
	public ModNotFoundException(String modName)
	{
		super(null, null);
		this.modName = modName;
	}

	public String getMessage()
	{
		return "Mod '"+modName+"' was not found.";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5671040280199985929L;

}
