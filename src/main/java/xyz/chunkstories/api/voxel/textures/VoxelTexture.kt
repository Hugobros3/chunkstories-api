//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.textures

import org.joml.Vector4fc

/** A VoxelTexture is a part of a Texture atlas contaning all the voxel textures
 * stitched together. The coordinates are common in all voxel texture atlases
 * (albedo, normal, ... )  */
interface VoxelTexture {
    val name: String

    /** Return the average color for this voxel texture  */
    val color: Vector4fc

    /** How many blocks does this texture span  */
    val textureScale: Int

    /** Is this texture animated ?  */
    val animationFrames: Int
}
