//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.generator.environment;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pipeline.Shader;
import io.xol.chunkstories.api.rendering.textures.Texture2D;

/** Helper interface regrouping all the look & feel specific traits of a World */
public interface WorldEnvironment {
	
	/** How 'wet' ( reflective ) should the world be rendered ? Within [0:1]. Can vary depending on camera position. */
	public abstract float getWorldWetness(Vector3dc cameraPosition);
	
	/** Returns the texture used to colour the grass & other opaque textures depicting vegetation, this texture is mapped to 1-1 to the world boundaries */
	public abstract Texture2D getGrassTexture(RenderingInterface renderer);
	
	/** Returns the sunlight color. Be advised, the sunlight is added to the shadow light. */
	public abstract Vector3fc getSunlightColor(Vector3dc cameraPosition);
	
	/** Returns the shadow ('ambient') color. Be advised, this is multiplied with the voxel sunlight data, so a pitch-black cave will not pick up any of this color. */
	public abstract Vector3fc getShadowColor(Vector3dc cameraPosition);
	
	/** Set-ups the shader constants used to render shadows */
	public default void setupShadowColors(RenderingInterface renderer, Shader shader) {

		Vector3dc cameraPosition = renderer.getCamera().getCameraPosition();
		
		shader.setUniform1f("shadowStrength", 1.0f);
		shader.setUniform3f("sunColor", getSunlightColor(cameraPosition));
		shader.setUniform3f("shadowColor", getShadowColor(cameraPosition));
	}
}
