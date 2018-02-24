//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.target;

/**
 * Different kinds of stuff qualify as a render target, mostly textures
 */
public interface RenderTarget
{
	public void resize(int width, int height);
	
	public int getWidth();
	
	public int getHeight();

	public boolean destroy();
	
	//Internal.
	public void attachAsDepth();
	
	public void attachAsColor(int colorAttachement);
}
