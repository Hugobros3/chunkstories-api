package io.xol.chunkstories.api.rendering.world;

import org.joml.Vector3fc;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pipeline.ShaderInterface;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Takes care of rendering the 'background' of a frame, typically using some sort of skybox or fancy props.
 *  Is also responsible to setup shader parameters, such as fog
 */
public interface SkyboxRenderer {
	
	public Vector3fc getSunPosition();
	
	public void render(RenderingInterface renderingContext);
	
	public void setupShader(ShaderInterface shaderInterface);
}
