package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;

/** A special VoxelContext outside of any physical realm; using id-relative or world-relative methods will throw exceptions. */
public class AbstractVoxelContext implements VoxelContext {

	private Voxel voxel;
	int metadata, blocklight, sunlight;
	
	public AbstractVoxelContext(Voxel voxel, int meta, int blocklight, int sunlight) {
		this.metadata = meta;
		this.blocklight = blocklight;
		this.sunlight = sunlight;
	}

	@Override
	public World getWorld() {
		return null;
	}
	
	@Override
	public int getData() {
		return VoxelFormat.format(0xDEAD, metadata, sunlight, blocklight);
	}

	@Override
	public int getNeightborData(int side) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Voxel getVoxel() {
		return voxel;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getZ() {
		return 0;
	}

	@Override
	public int getMetaData() {
		return metadata;
	}

	@Override
	public int getSunlight() {
		return sunlight;
	}

	@Override
	public int getBlocklight() {
		return blocklight;
	}
	
	public void setVoxel(Voxel voxel) {
		this.voxel = voxel;
	}

	public void setMetaData(int metadata) {
		this.metadata = metadata;
	}

	public void setSunlight(int sunlight) {
		this.sunlight = sunlight;
	}

	@Override
	public VoxelContext getNeightbor(int side) {
		throw new UnsupportedOperationException("getNeightbor()");
	}
}
