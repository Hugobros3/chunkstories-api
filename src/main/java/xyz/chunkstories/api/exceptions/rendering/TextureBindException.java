//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.rendering;

import xyz.chunkstories.api.graphics.Texture;

public class TextureBindException extends RenderingException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7397862685160889891L;

	Texture texture;
	String extraInformation;

	public TextureBindException(Texture texture) {
		this.texture = texture;
	}

	public TextureBindException(Texture texture, String extraInformation) {
		this.texture = texture;
		this.extraInformation = extraInformation;
	}

	@Override
	public String getMessage() {
		if (extraInformation != null)
			return "Texture " + texture + " couldn't be bound : " + extraInformation;
		return "Texture " + texture + " couldn't be bound.";
	}
}
