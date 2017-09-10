package io.xol.chunkstories.api.voxel.models;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Used to create intricate models, with floating point coordinates. Consumes more VRAM. */
public interface VoxelBakerHighPoly extends VoxelBakerCommon
{
	/** Begins a vertex at that floating point coordinate */
	public void beginVertex(float f0, float f1, float f2);
	
	/** Overload for beginVertex(f0, f1, f2) */
	public void beginVertex(Vector3fc vertex);

	/** Overload for beginVertex(f0, f1, f2) */
	public void beginVertex(Vector3dc vertex);
}
