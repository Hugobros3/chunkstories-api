package io.xol.chunkstories.api.rendering.voxel;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Only able to have whole/integer vertice coordinates, saves VRAM. */
public interface VoxelBakerCubic extends VoxelBakerCommon
{
	/** Begins a vertex at those integer coordinates */
	public void beginVertex(int i0, int i1, int i2);
}
