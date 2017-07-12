package io.xol.chunkstories.api.voxel.textures;

import org.joml.Vector4fc;

public interface VoxelTexture
{
	Vector4fc getColor();

	int getAtlasS();

	int getAtlasT();

	int getAtlasOffset();

	int getTextureScale();

	String getName();
}
