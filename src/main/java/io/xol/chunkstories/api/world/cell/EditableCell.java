//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.cell;

import io.xol.chunkstories.api.voxel.Voxel;

/** A cell we have the right to edit. */
public interface EditableCell extends CellData {

	public void setVoxel(Voxel voxel);

	public void setMetaData(int metadata);

	public void setSunlight(int sunlight);

	public void setBlocklight(int blocklight);
}
