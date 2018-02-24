//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.world.WorldRenderer.RenderingPass;

public interface EntityRenderer<E extends EntityRenderable>
{
	/** Renders all entities */
	public int renderEntities(RenderingInterface renderingInterface, RenderingIterator<E> renderableEntitiesIterator);
	
	/** By default entities only render to the shadow and opaque passes, but you are free to change that */
	public default boolean renderInPass(RenderingPass renderingPass) {
		return renderingPass == RenderingPass.SHADOW || renderingPass == RenderingPass.NORMAL_OPAQUE;
	}
	
	public void freeRessources();
}
