package io.xol.chunkstories.api.rendering;

import org.joml.Vector4fc;

import io.xol.chunkstories.api.rendering.textures.Texture2D;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface GuiRenderer
{
	public void drawBoxWindowsSpace(float startX, float startY, float endX, float endY, float textureStartX, float textureStartY, float textureEndX, float textureEndY, Texture2D texture, boolean alpha, boolean textured, Vector4fc color);

	public void drawBoxWindowsSpaceWithSize(float startX, float startY, float width, float height, float textureStartX, float textureStartY, float textureEndX, float textureEndY, Texture2D texture, boolean alpha, boolean textured, Vector4fc color);

	public void drawBox(float startX, float startY, float endX, float endY, float textureStartX, float textureStartY, float textureEndX, float textureEndY, Texture2D texture, boolean alpha, boolean textured, Vector4fc color);

	//TODO rename
	/** Flushes what's remaining to draw */
	public void drawBuffer();
}