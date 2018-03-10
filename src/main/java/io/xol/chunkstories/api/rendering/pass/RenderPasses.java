//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.pass;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.world.WorldRenderer;

public interface RenderPasses {
	public RenderingInterface getRenderingInterface();
	
	public WorldRenderer getWorldRenderer();
	
	public void registerRenderPass(RenderPass pass);

	public RenderPass getRenderPass(String name);
	
	public RenderPass getCurrentPass();
}
