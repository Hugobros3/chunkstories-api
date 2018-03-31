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
	public int renderEntities(RenderingInterface renderer, RenderingIterator<E> renderableEntitiesIterator) {
		
		boolean shadow = renderer.getCurrentPass().name.startsWith("shadow");
		Shader shader = /*shadow ? renderer.currentShader() : */renderer.useShader("entities");

		//entitiesShader.setUniform1f("wetness", world.getGenerator().getEnvironment().getWorldWetness(renderer.getCamera().getCameraPosition()));

		renderer.currentShader().setUniform1f("useColorIn", 0.0f);
		renderer.currentShader().setUniform1f("useNormalIn", 1.0f);

		renderer.getCamera().setupShader(shader);
		renderer.getWorldRenderer().setupShaderUniforms(shader);
		
		return 0;
	}
	
	/** By default entities only render to the shadow and opaque passes, but you are free to change that */
	public boolean renderInPass(RenderPass renderingPass) {
		return renderingPass.name.startsWith("shadow") || renderingPass.name.startsWith("opaque");
	}
	
	public abstract void freeRessources();
}
