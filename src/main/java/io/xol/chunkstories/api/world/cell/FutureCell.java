//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.cell;

import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.world.World;

import javax.annotation.Nullable;

/** A VoxelContext representing a possible future state for a voxel cell. */
public class FutureCell extends Cell implements CellData, EditableCell {

	World world;

	public FutureCell(World world, int x, int y, int z, @Nullable Voxel voxel) {
		this(world, x, y, z, voxel, -1, -1, -1);
	}

	public FutureCell(World world, int x, int y, int z, @Nullable Voxel voxel, int meta, int blocklight, int sunlight) {
		super(x, y, z, voxel, meta, blocklight, sunlight);
		this.world = world;
	}

	public FutureCell(CellData ogContext) {
		super(ogContext.getX(), ogContext.getY(), ogContext.getZ(), ogContext.getVoxel(), ogContext.getMetaData(),
				ogContext.getBlocklight(), ogContext.getSunlight());
		this.world = ogContext.getWorld();
	}

	@Deprecated
	public int getData() {
		return VoxelFormat.format(world.getContentTranslator().getIdForVoxel(getVoxel()), metadata, sunlight,
				blocklight);
	}

	@Deprecated
	public int getNeightborData(int side) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellData getNeightbor(int side) {
		throw new UnsupportedOperationException("getNeightbor()");
	}

	@Override
	public World getWorld() {
		return world;
	}
}
