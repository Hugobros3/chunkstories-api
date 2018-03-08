package io.xol.chunkstories.api.rendering.world.chunk;

import io.xol.chunkstories.api.rendering.CameraInterface;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.world.WorldRenderer.FarTerrainRenderer.ReadyVoxelMeshesMask;

public interface ChunksRenderer {

	/** Computes wich parts of the world could be seen by said entity */
	public void updatePVSSet(CameraInterface camera);

	public void renderChunks(RenderingInterface renderingInterface);

	public void renderChunksExtras(RenderingInterface renderingInterface);

	public ReadyVoxelMeshesMask getRenderedChunksMask(CameraInterface camera);

}