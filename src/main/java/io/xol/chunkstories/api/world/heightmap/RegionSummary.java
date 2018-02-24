//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.heightmap;

import java.util.Iterator;

import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.chunk.WorldUser;

public interface RegionSummary
{
	public boolean isLoaded();
	
	public Fence waitForLoading();
	
	public boolean registerUser(WorldUser user);

	public boolean unregisterUser(WorldUser user);

	public Iterator<WorldUser> getSummaryUsers();

	public int getHeight(int x, int z);

	public CellData getTopCell(int x, int z);
	
	public void setTopCell(CellData data);

	public int getRegionX();
	
	public int getRegionZ();
}
