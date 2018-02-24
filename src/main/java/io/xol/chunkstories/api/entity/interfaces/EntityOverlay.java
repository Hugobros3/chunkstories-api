//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.rendering.RenderingInterface;

/**
 * An entity that draws 2d stuff on screen upon rendering
 */
public interface EntityOverlay
{
	public abstract void drawEntityOverlay(RenderingInterface renderingInterface);
}
