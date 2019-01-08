//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell;

import xyz.chunkstories.api.voxel.Voxel;

/** A getCell we have the right to edit. */
public interface EditableCell extends CellData {

	public void setVoxel(Voxel voxel);

	public void setMetaData(int metadata);

	public void setSunlight(int sunlight);

	public void setBlocklight(int blocklight);
}
