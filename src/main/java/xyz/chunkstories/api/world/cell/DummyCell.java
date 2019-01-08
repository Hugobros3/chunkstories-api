//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell;

import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.voxel.VoxelFormat;
import xyz.chunkstories.api.world.World;

/** A special getCell outside of any physical realm; using id-relative or
 * world-relative methods will throw exceptions. */
public class DummyCell extends Cell implements CellData, EditableCell {

	public DummyCell(int x, int y, int z, Voxel voxel, int meta, int blocklight, int sunlight) {
		super(x, y, z, voxel, meta, blocklight, sunlight);
	}

	@Override
	public World getWorld() {
		return null;
	}

	@Deprecated
	public int getData() {
		// Make up ids
		return VoxelFormat.format(0xDEAD, metadata, sunlight, blocklight);
	}

	@Deprecated
	public int getNeightborData(int side) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellData getNeightbor(int side) {
		throw new UnsupportedOperationException("getNeightbor()");
	}
}
