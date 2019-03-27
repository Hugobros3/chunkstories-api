package xyz.chunkstories.api.world.chunk

import xyz.chunkstories.api.voxel.components.VoxelComponent

interface FreshChunkCell : ChunkCell {
    fun registerComponent(name: String, component: VoxelComponent)
}
