//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.textures;

import java.nio.ByteBuffer;

public interface Texture3D extends Texture {

	/** Overload of uploadTextureData() using level = 0 */
	public boolean uploadTextureData(int width, int height, int depth, ByteBuffer data);

	/**
	 * Uploads image data into the said level. Actual upload might be delayed,
	 * thread-safe ( in any other thread than rendering, upload will be scheduled
	 * for later ).
	 */
	public boolean uploadTextureData(int width, int height, int depth, int level, ByteBuffer data);

	public void bind();

	/** Determines if a texture will loop arround itself or clamp to it's edges */
	public void setTextureWrapping(boolean on);

	/**
	 * Enables the use of 'blur' on the texture. Disable for a crips, pixelated
	 * effect
	 */
	public void setLinearFiltering(boolean on);

	/** Enables or disable the user of mip-maps */
	// public void setMipMapping(boolean on);

	/** Mimaps only in this range will be used */
	// public void setMipmapLevelsRange(int baseLevel, int maxLevel);

	/** Bakes the mipmaps of this texture, based on the Level0 texture */
	// public void computeMipmaps();

	public int getWidth();

	public int getHeight();

	public int getDepth();

	/** Computes actual maximal mipmap level this texture will hold */
	// public int getMaxMipmapLevel();

	public long getVramUsage();
}