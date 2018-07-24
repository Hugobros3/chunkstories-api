//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

import io.xol.chunkstories.api.content.mods.ModInfo;

public class MalformedModTxtException extends ModLoadFailureException {
	public MalformedModTxtException(ModInfo ok) {
		super(null, "Malformed txt info file or  missing one.");
	}

	private static final long serialVersionUID = 9218958020915706786L;

}
