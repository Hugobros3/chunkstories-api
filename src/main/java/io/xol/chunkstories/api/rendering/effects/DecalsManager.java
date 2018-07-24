//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.effects;

import org.joml.Vector3dc;

public interface DecalsManager {
	public void drawDecal(Vector3dc position, Vector3dc orientation, Vector3dc size, String decalName);
}
