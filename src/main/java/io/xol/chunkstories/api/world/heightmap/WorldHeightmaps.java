//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.heightmap;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldUser;
import io.xol.chunkstories.api.world.cell.CellData;

import javax.annotation.Nullable;

public interface WorldHeightmaps {
	
	/** Return the associated World */
	public World getWorld();
	
	/**
	 * Aquires a heightmap and registers an user, triggering a load operation for the heightmap and preventing it to unload until all the users
	 *  either unregisters or gets garbage collected and their references nulls out.
	 */
	public Heightmap acquireHeightmap(WorldUser worldUser, int regionX, int regionZ);
	
	/**
	 * Aquires a heightmap and registers an user, triggering a load operation for the heightmap and preventing it to unload until all the users
	 *  either unregisters or gets garbage collected and their references nulls out.
	 */
	public Heightmap acquireHeightmapChunkCoordinates(WorldUser worldUser, int chunkX, int chunkZ);
	
	/**
	 * Aquires a heightmap and registers an user, triggering a load operation for the heightmap and preventing it to unload until all the users
	 *  either unregisters or gets garbage collected and their references nulls out.
	 */
	public Heightmap acquireHeightmapWorldCoordinates(WorldUser worldUser, int worldX, int worldZ);
	
	/**
	 * Aquires a heightmap and registers an user, triggering a load operation for the heightmap and preventing it to unload until all the users
	 *  either unregisters or gets garbage collected and their references nulls out.
	 */
	public Heightmap acquireHeightmapLocation(WorldUser worldUser, Location location);
	
	/**
	 * Returns either null or a valid, entirely loaded heightmap if the aquireHeightmap method was called and it had time to load and there is still one user using it
	 */
	@Nullable
	public Heightmap getHeightmap(int regionX, int regionZ);
	
	/**
	 * Returns either null or a valid, entirely loaded heightmap if the aquireHeightmap method was called and it had time to load and there is still one user using it
	 */
	@Nullable
	public Heightmap getHeightmapChunkCoordinates(int chunkX, int chunkZ);
	
	/**
	 * Returns either null or a valid, entirely loaded heightmap if the aquireHeightmap method was called and it had time to load and there is still one user using it
	 */
	@Nullable
	public Heightmap getHeightmapWorldCoordinates(int worldX, int worldZ);
	
	/**
	 * Returns either null or a valid, entirely loaded heightmap if the aquireHeightmap method was called and it had time to load and there is still one user using it
	 */
	@Nullable
	public Heightmap getHeightmapLocation(Location location);
	
	public int getHeightAtWorldCoordinates(int worldX, int worldZ);
	
	public CellData getTopCellAtWorldCoordinates(int worldX, int worldZ);
}
