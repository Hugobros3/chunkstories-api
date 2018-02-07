package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;

/** A VoxelContext representing a possible future state for a voxel cell. */
public class FutureVoxelContext extends Location implements VoxelContext, EditableVoxelContext {

	private Voxel voxel;
	int ix, iy, iz;
	int metadata, blocklight, sunlight;

	public FutureVoxelContext(World world, int x, int y, int z, Voxel voxel) {
		this(world, x, y, z, voxel, 0, 0, 0);
	}
	
	public FutureVoxelContext(World world, int x, int y, int z, Voxel voxel, int meta, int blocklight, int sunlight) {
		super(world, x, y, z);
		this.ix = x;
		this.iy = y;
		this.iz = z;
		
		this.metadata = meta;
		this.blocklight = blocklight;
		this.sunlight = sunlight;
	}
	
	public FutureVoxelContext(VoxelContext ogContext) {
		this(ogContext.getWorld(), ogContext.getX(), ogContext.getY(), ogContext.getZ(), ogContext.getVoxel(), ogContext.getMetaData(), ogContext.getBlocklight(), ogContext.getSunlight());
	}

	@Override
	public int getData() {
		return VoxelFormat.format(world.getContentTranslator().getIdForVoxel(getVoxel()), metadata, sunlight, blocklight);
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
		return ix;
	}

	@Override
	public int getY() {
		return iy;
	}

	@Override
	public int getZ() {
		return iz;
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
	
	public void setBlocklight(int blocklight) {
		this.blocklight = blocklight;
	}

	@Override
	public VoxelContext getNeightbor(int side) {
		throw new UnsupportedOperationException("getNeightbor()");
	}
}
