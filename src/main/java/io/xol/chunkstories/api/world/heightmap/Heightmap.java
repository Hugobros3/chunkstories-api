//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.heightmap;

import java.util.Iterator;

import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.WorldUser;
import io.xol.chunkstories.api.world.cell.CellData;

/**
 * Represents the topmost cell for 256 * 256 area. X and Z coordinates match
 * those of regions
 */
public interface Heightmap {
	public final int NO_DATA = -1;

	/** Get the region-space X coordinate for the begining of this heightmap data */
	public int getRegionX();

	/** Get the region-space Z coordinate for the begining of this heightmap data */
	public int getRegionZ();

	public boolean isLoaded();

	public Fence waitForLoading();

	public boolean registerUser(WorldUser user);

	public boolean unregisterUser(WorldUser user);

	public Iterator<WorldUser> getUsers();

	/**
	 * Return the height of the topmost block or NO_DATA is no data is yet available
	 */
	public int getHeight(int x, int z);

	public CellData getTopCell(int x, int z);

	public void setTopCell(CellData data);

	public Fence save();
}
