package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.voxel.Voxel;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Extends the notion of voxel context to add in the editability */
public interface EditableVoxelContext extends VoxelContext {
	
	public void setVoxel(Voxel voxel);

	public void setMetaData(int metadata);

	public void setSunlight(int sunlight);
	
	public void setBlocklight(int blocklight);
}
