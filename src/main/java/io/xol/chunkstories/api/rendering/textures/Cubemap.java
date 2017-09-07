package io.xol.chunkstories.api.rendering.textures;

import io.xol.chunkstories.api.rendering.target.RenderTarget;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface Cubemap extends Texture {

	/** Returns the size of the cubes that make up the cubemap */
	public int getSize();

	public RenderTarget getFace(int f);

	public long getVramUsage();
}