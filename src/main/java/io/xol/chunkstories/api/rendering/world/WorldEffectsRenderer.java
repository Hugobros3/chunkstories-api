//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world;

import io.xol.chunkstories.api.rendering.RenderingInterface;

/**
 * Handles weather and other whole-world effects
 */
public interface WorldEffectsRenderer
{
	/**
	 * Called when rendering a frame
	 */
	public void renderEffects(RenderingInterface renderingContext);

	/**
	 * Called each tick (default is 60 tps)
	 */
	public void tick();

	/**
	 * Called when cleaning up ( deletes vbos, frees textures )
	 */
	public void destroy();
}