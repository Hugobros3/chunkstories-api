//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world.chunk;

import io.xol.chunkstories.api.rendering.voxel.VoxelBakerCubic;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerHighPoly;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.LodLevel;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.ShadingType;
import io.xol.chunkstories.api.voxel.VoxelSide;

/** This interface is the context in which chunks are rendered, it's implemented by the thread-pool rendering the chunks */
public interface ChunkRenderer
{
	public VoxelBakerHighPoly getHighpolyBakerFor(LodLevel lodLevel, ShadingType renderPass);
	
	public VoxelBakerCubic getLowpolyBakerFor(LodLevel lodLevel, ShadingType renderPass);
	
	public interface ChunkRenderContext {
		public boolean isTopChunkLoaded();
		public boolean isBottomChunkLoaded();
		public boolean isLeftChunkLoaded();
		public boolean isRightChunkLoaded();
		public boolean isFrontChunkLoaded();
		public boolean isBackChunkLoaded();
		
		/** Returns the INTERNAL X coordinate of the voxel currently being rendered, in the chunk (0-31) */
		public int getRenderedVoxelPositionInChunkX();
		
		/** Returns the INTERNAL Y coordinate of the voxel currently being rendered, in the chunk (0-31) */
		public int getRenderedVoxelPositionInChunkY();
		
		/** Returns the INTERNAL Z coordinate of the voxel currently being rendered, in the chunk (0-31) */
		public int getRenderedVoxelPositionInChunkZ();
		
		public VoxelLighter getCurrentVoxelLighter();
		
		public interface VoxelLighter {
			/** Returns a value between 0 and 15 if the block is non-opaque, -1 if it is */
			public byte getSunlightLevelForCorner(VoxelSide.Corners corner);

			/** Returns a value between 0 and 15 if the block is non-opaque, -1 if it is */
			public byte getBlocklightLevelForCorner(VoxelSide.Corners corner);
			
			public byte getAoLevelForCorner(VoxelSide.Corners corner);

			public byte getSunlightLevelInterpolated(float vertX, float vertY, float vertZ);
			public byte getBlocklightLevelInterpolated(float vertX, float vertY, float vertZ);
			public byte getAoLevelInterpolated(float vertX, float vertY, float vertZ);
		}
	}
}
