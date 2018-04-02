//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pass.RenderPass;
import io.xol.chunkstories.api.rendering.shader.Shader;

public abstract class EntityRenderer<E extends EntityRenderable>
{
	/** Override this to implement your own entity render */
	@SuppressWarnings("unused")
	public int renderEntities(RenderingInterface renderer, RenderingIterator<E> renderableEntitiesIterator) {
		Shader shader = renderer.useShader("entities"); // The new and improved base 'entities' shader supports 
		int c = 0;
		
		for(E entity : renderableEntitiesIterator.getElementsInFrustrumOnly()) {
			c++;

			//do your rendering here
		}
		return c;
	}
	
	/** By default entities only render to the shadow and opaque passes, but you are free to change that */
	public boolean renderInPass(RenderPass renderingPass) {
		return renderingPass.name.startsWith("shadow") || renderingPass.name.startsWith("opaque");
	}
	
	public abstract void freeRessources();
}
