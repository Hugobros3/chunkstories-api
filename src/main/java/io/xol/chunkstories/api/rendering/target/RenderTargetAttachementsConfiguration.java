//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.target;

/** A semi-obvious proxy for FBO configuration in OGL, abstracting it better would be hard, and 
 * it is'nt that often you find yourself messing with framebuffer output, so isn't that a big deal... is it ?
 * Created using a RenderTargetManager.
 */
public interface RenderTargetAttachementsConfiguration {

	/** Enables or disables specific render targets in this configuration */
	public void setEnabledRenderTargets(boolean... targets);

	public void setDepthAttachement(RenderTarget depthAttachement);

	public void setColorAttachement(int index, RenderTarget colorAttachement);

	public void setColorAttachements(RenderTarget... colorAttachements);

	/** Resizes the framebuffer output ( and associated textures ) */
	public void resizeFBO(int w, int h);

	/** Destroys the configuration. MUST BE CALLED OR MEMORY LEAK BAD OK BAD 
	 * 	TexturesToo will destroys the linked RenderTarget, might be usefull but consider the side effects. */
	public void destroy(boolean texturesToo);
}