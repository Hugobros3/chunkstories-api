//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

import io.xol.chunkstories.api.content.mods.Mod;

public class ConflictingExternalCodeException extends ModLoadFailureException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933179574010715068L;

	public ConflictingExternalCodeException(Mod mod, String message)
	{
		super(mod, message);
	}

}
