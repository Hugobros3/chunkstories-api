//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world;

import org.joml.Vector3fc;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pipeline.Shader;

/** Takes care of rendering the 'background' of a frame, typically using some sort of skybox or fancy props.
 *  Is also responsible to setup shader parameters, such as fog
 */
public interface SkyRenderer {
	
	public void render(RenderingInterface renderer);
	
	public void setupShader(Shader shaderInterface);

	public Vector3fc getSunPosition();
	
	/** Returns daytime (0-10'000) */
	public float getDayTime();
}
