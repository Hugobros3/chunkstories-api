//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel.textures;

import org.joml.Vector4fc;

/** A VoxelTexture is a part of a Texture atlas contaning all the voxel textures
 * stitched together. The coordinates are common in all voxel texture atlases
 * (albedo, normal, ... ) */
public interface VoxelTexture {
	public String getName();

	/** Return the average color for this voxel texture */
	public Vector4fc getColor();

	/** At which pixel (X) in the atlas does this texture start */
	public int getAtlasS();

	/** At which pixel (Y) in the atlas does this texture start */
	public int getAtlasT();

	/** How many pixels wide is the texture */
	public int getAtlasOffset();

	/** How many blocks does this texture span */
	public int getTextureScale();

	/** How many animation frames is there for this ? ( ration of height/width in
	 * the texture ) */
	public int getAnimationFrames();
}
