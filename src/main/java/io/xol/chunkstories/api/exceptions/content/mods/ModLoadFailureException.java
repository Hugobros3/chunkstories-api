//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

import io.xol.chunkstories.api.content.mods.Mod;

public class ModLoadFailureException extends Exception {
	private static final long serialVersionUID = -3181028531069214061L;

	Mod mod;
	String message;

	public ModLoadFailureException(Mod mod, String message) {
		this.mod = mod;
		this.message = message;
	}

	public String getMessage() {
		return "Mod '" + mod + "' failed to load : " + message;
	}
}
