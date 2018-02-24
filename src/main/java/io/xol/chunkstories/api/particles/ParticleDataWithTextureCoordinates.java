//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.particles;

public interface ParticleDataWithTextureCoordinates {
	
	public float getTextureCoordinateXTopLeft();
	public float getTextureCoordinateXTopRight();
	public float getTextureCoordinateXBottomLeft();
	public float getTextureCoordinateXBottomRight();
	
	public float getTextureCoordinateYTopLeft();
	public float getTextureCoordinateYTopRight();
	public float getTextureCoordinateYBottomLeft();
	public float getTextureCoordinateYBottomRight();
}
