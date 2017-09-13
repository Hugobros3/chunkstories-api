package io.xol.chunkstories.api.voxel.models;

import io.xol.chunkstories.api.Content;
import io.xol.chunkstories.api.voxel.models.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.chunk.Chunk;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Represents a voxel .model file loaded by the engine. Look up the syntax on the wiki for more information. */
public interface VoxelModel extends VoxelRenderer
{
	public String getName();
	
	/** The actual rendering method used internally, you can use it to divert the rendering to another baker than the default one (opaque & any lod) */
	public int renderInto(VoxelBakerHighPoly baker, ChunkRenderContext bakingContext, VoxelContext info, Chunk chunk, int x, int y, int z);

	public int getSizeInVertices();

	public boolean[][] getCulling();

	public String[] getTexturesNames();

	public int[] getTexturesOffsets();

	public float[] getVertices();

	public float[] getTexCoords();

	public float[] getNormals();

	public byte[] getExtra();

	public float getJitterX();

	public float getJitterY();

	public float getJitterZ();

	public Content.Voxels.VoxelModels store();

}