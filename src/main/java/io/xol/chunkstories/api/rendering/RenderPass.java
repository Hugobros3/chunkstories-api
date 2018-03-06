package io.xol.chunkstories.api.rendering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xol.chunkstories.api.rendering.textures.Texture;

public abstract class RenderPass {

	public final RenderingPipeline pipeline;
	public final String name;
	public final List<String> requires; //contains the input buffers you want to be given in the form renderPassRequired.bufferRequired and end with an ! to ask to be able to write to that texture
	public final List<String> exports; //Contains the output buffers you expose to the other passes. you can also pass inputs

	public RenderPass(RenderingPipeline pipeline, String name, String[] requires, String[] exports) {
		this.pipeline = pipeline;
		this.name = name;
		
		this.requires = new ArrayList<>(Arrays.asList(requires));
		this.exports = new ArrayList<>(Arrays.asList(exports));
	}
	
	/** Called when all the required render passes have been initialized, gives you the inputs you asked */
	public abstract void resolvedInputs(Map<String, Texture> inputs);

	/** Actual buffers shoud end up here */
	public final Map<String, Texture> resolvedOutputs = new HashMap<>();

	public abstract void render(RenderingInterface renderer);

	public abstract void onScreenResize(int width, int height);
}
