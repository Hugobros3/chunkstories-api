//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui;

import org.joml.Vector4fc;

import io.xol.chunkstories.api.rendering.textures.Texture2D;

import javax.annotation.Nullable;

public interface GuiRenderer {
	public void drawBoxWindowsSpace(float startX, float startY, float endX, float endY, float textureStartX,
			float textureStartY, float textureEndX, float textureEndY, @Nullable Texture2D texture, boolean alpha,
			boolean textured, @Nullable Vector4fc color);

	public void drawBoxWindowsSpaceWithSize(float startX, float startY, float width, float height, float textureStartX,
			float textureStartY, float textureEndX, float textureEndY, @Nullable Texture2D texture, boolean alpha,
			boolean textured, @Nullable Vector4fc color);

	public void drawBox(float startX, float startY, float endX, float endY, float textureStartX, float textureStartY,
			float textureEndX, float textureEndY, @Nullable Texture2D texture, boolean alpha, boolean textured,
			@Nullable Vector4fc color);

	public void drawCorneredBoxTiled(float posx, float posy, float width, float height, int cornerSize,
			Texture2D texture, int textureSize, int scale);

	// TODO rename
	/** Flushes what's remaining to draw */
	public void drawBuffer();
}