package io.xol.chunkstories.api.world.cell;

import io.xol.chunkstories.api.voxel.Voxel;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** A cell we have the right to edit. */
public interface EditableCell extends CellData {
	
	public void setVoxel(Voxel voxel);

	public void setMetaData(int metadata);

	public void setSunlight(int sunlight);
	
	public void setBlocklight(int blocklight);
}
