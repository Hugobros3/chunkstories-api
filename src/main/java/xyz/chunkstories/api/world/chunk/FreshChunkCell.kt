//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.voxel.components.VoxelComponent

interface FreshChunkCell : ChunkCell {
    fun registerComponent(name: String, component: VoxelComponent)
}
