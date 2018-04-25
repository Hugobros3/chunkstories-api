//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pass.RenderPass;

public abstract class EntityRenderer<E extends Entity>
{
	/** Override this to implement your own entity render */
	public abstract int renderEntities(RenderingInterface renderer, RenderingIterator<E> renderableEntitiesIterator);
	
	/** By default entities only render to the shadow and opaque passes, but you are free to change that */
	public boolean renderInPass(RenderPass renderingPass) {
		return renderingPass.name.startsWith("shadow") || renderingPass.name.startsWith("opaque");
	}
	
	public abstract void freeRessources();
}
