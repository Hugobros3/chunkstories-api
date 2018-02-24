//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.pipeline;

import io.xol.chunkstories.api.rendering.RenderingInterface;

/**
 * Abstracts the shaders uniforms ( excluding textures, see TexturingConfiguration )
 */
public interface UniformsConfiguration
{
	/**
	 * Used by RenderingCommands to determine if they can be merged together and instanced
	 */
	public boolean isCompatibleWith(UniformsConfiguration uniformsConfiguration);
	
	/**
	 * Setups the uniforms for the shader
	 */
	public void setup(RenderingInterface renderingInterface);
}
