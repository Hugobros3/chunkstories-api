//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.textures;

/** Completely abstracted-out Texture class. Represents some kind of picture on the GPU. */
public interface Texture {

	public TextureFormat getType();

	public void bind();

	/** Unloads the texture from memory and frees the associated memory. Not actually recommanded since GC works on texture objects and will clear linked
	 * GPU memory as well.
	 */
	public boolean destroy();

	/** Returns the VRAM usage, in bytes */
	public long getVramUsage();
}