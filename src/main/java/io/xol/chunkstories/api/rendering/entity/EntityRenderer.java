//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.rendering.RenderPass;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public interface EntityRenderer<E extends EntityRenderable>
{
	/** Renders all entities */
	public int renderEntities(RenderingInterface renderingInterface, RenderingIterator<E> renderableEntitiesIterator);
	
	/** By default entities only render to the shadow and opaque passes, but you are free to change that */
	public default boolean renderInPass(RenderPass renderingPass) {
		return renderingPass.name.startsWith("shadow") || renderingPass.name.startsWith("opaque");
	}
	
	public void freeRessources();
}
