//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.text;

import io.xol.chunkstories.api.rendering.Renderable;

public interface TextMesh extends Renderable {
	public String getText();

	public void destroy();
}
