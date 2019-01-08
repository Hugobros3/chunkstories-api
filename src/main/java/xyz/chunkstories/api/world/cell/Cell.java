//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell;

import xyz.chunkstories.api.voxel.Voxel;

import javax.annotation.Nullable;

public abstract class Cell implements CellData {
	final protected int x, y, z;

	@Nullable
	protected Voxel voxel;
	protected int metadata, blocklight, sunlight;

	public Cell(int x, int y, int z, @Nullable Voxel voxel, int meta, int blocklight, int sunlight) {
		this.x = x;
		this.y = y;
		this.z = z;

		this.voxel = voxel;
		this.metadata = meta;
		this.blocklight = blocklight;
		this.sunlight = sunlight;

		// if(!voxel.getRegisteredComponentName().equals("air"))
		// System.out.println(voxel.getRegisteredComponentName());
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public Voxel getVoxel() {
		return voxel;
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

	public String toString() {
		return "[Cell " + x + ", " + y + ", " + z + " " + voxel + " " + sunlight + ":" + blocklight + ":" + metadata + "]";
	}
}
