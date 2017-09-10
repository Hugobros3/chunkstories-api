package io.xol.chunkstories.api.voxel.models;

import org.joml.Vector3d;
import org.joml.Vector3f;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Used to create intricate models, with floating point coordinates. Consumes more VRAM. */
public interface VoxelBakerHighPoly extends VoxelBakerCommon
{
	public void beginVertex(float f0, float f1, float f2);
	
	public void beginVertex(Vector3f vertex);
	
	public void beginVertex(Vector3d vertex);
}
