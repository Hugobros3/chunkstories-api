//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.pass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.shader.Shader;
import io.xol.chunkstories.api.rendering.shader.Shader.SamplerType;
import io.xol.chunkstories.api.rendering.textures.ArrayTexture;
import io.xol.chunkstories.api.rendering.textures.Cubemap;
import io.xol.chunkstories.api.rendering.textures.Texture;
import io.xol.chunkstories.api.rendering.textures.Texture1D;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.textures.Texture3D;

public abstract class RenderPass {

	public final RenderPasses pipeline;
	public final String name;
	public final List<String> requires; // contains the input buffers you want to be given in the form
										// renderPassRequired.bufferRequired and end with an ! to ask to be able to
										// write to that texture
	public final List<String> exports; // Contains the output buffers you expose to the other passes. you can also pass
										// inputs

	public RenderPass(RenderPasses pipeline, String name, String[] requires, String[] exports) {
		this.pipeline = pipeline;
		this.name = name;

		this.requires = new ArrayList<>(Arrays.asList(requires));
		this.exports = new ArrayList<>(Arrays.asList(exports));
	}

	/** Called when all the required render passes have been initialized and
	 * resolvedInputs is filled with them. */
	public abstract void onResolvedInputs();

	/** Inputs you requested are found here */
	public final Map<String, Texture> resolvedInputs = new HashMap<>();

	/** Actual buffers shoud end up here */
	public final Map<String, Texture> resolvedOutputs = new HashMap<>();

	public abstract void render(RenderingInterface renderer);

	public abstract void onScreenResize(int width, int height);

	/** Called when switching shader in the pass, you can setup any
	 * pass/shader-specific uniforms that would be required during this pass */
	public void setupShader(RenderingInterface renderer, Shader shader) {

		renderer.getCamera().setupShader(shader);
		renderer.getWorldRenderer().setupShaderUniforms(shader);

		renderer.currentShader().setUniform1i("isShadowPass", 0);
	}

	/** Called when switching shader in the pass, will automatically try to bind the
	 * available textures to the sampler */
	public void autoBindInputs(RenderingInterface renderer, Shader shader) {
		for (Entry<String, SamplerType> e : shader.samplers().entrySet()) {

			Texture input = resolvedInputs.get(e.getKey());

			if (input != null && input.getSamplerType() == e.getValue()) {
				switch (input.getSamplerType()) {
				case ARRAY_TEXTURE_2D:
					renderer.bindArrayTexture(e.getKey(), (ArrayTexture) input);
					break;
				case CUBEMAP:
					renderer.bindCubemap(e.getKey(), (Cubemap) input);
					break;
				case TEXTURE_1D:
					renderer.bindTexture1D(e.getKey(), (Texture1D) input);
					break;
				case TEXTURE_2D:
					renderer.bindTexture2D(e.getKey(), (Texture2D) input);
					break;
				case TEXTURE_3D:
					renderer.bindTexture3D(e.getKey(), (Texture3D) input);
					break;
				default:
					break;

				}
			}
		}
	}
}
