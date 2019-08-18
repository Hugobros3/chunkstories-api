//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.heightmap

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldUser
import xyz.chunkstories.api.world.cell.Cell

interface WorldHeightmapsManager {
    /** Return the associated World  */
    val world: World

    /** Aquires a heightmap and registers an user, triggering a load operation for
     * the heightmap and preventing it to unload until all the users either
     * unregisters or gets garbage collected and their references nulls out.  */
    fun acquireHeightmap(worldUser: WorldUser, regionX: Int, regionZ: Int): Heightmap

    /** Aquires a heightmap and registers an user, triggering a load operation for
     * the heightmap and preventing it to unload until all the users either
     * unregisters or gets garbage collected and their references nulls out.  */
    fun acquireHeightmapChunkCoordinates(worldUser: WorldUser, chunkX: Int, chunkZ: Int): Heightmap

    /** Aquires a heightmap and registers an user, triggering a load operation for
     * the heightmap and preventing it to unload until all the users either
     * unregisters or gets garbage collected and their references nulls out.  */
    fun acquireHeightmapWorldCoordinates(worldUser: WorldUser, worldX: Int, worldZ: Int): Heightmap

    /** Aquires a heightmap and registers an user, triggering a load operation for
     * the heightmap and preventing it to unload until all the users either
     * unregisters or gets garbage collected and their references nulls out.  */
    fun acquireHeightmapLocation(worldUser: WorldUser, location: Location): Heightmap

    /** Returns either null or a valid, entirely loaded heightmap if the
     * aquireHeightmap method was called and it had time to load and there is still
     * one user using it  */
    fun getHeightmap(regionX: Int, regionZ: Int): Heightmap?

    /** Returns either null or a valid, entirely loaded heightmap if the
     * aquireHeightmap method was called and it had time to load and there is still
     * one user using it  */
    fun getHeightmapChunkCoordinates(chunkX: Int, chunkZ: Int): Heightmap?

    /** Returns either null or a valid, entirely loaded heightmap if the
     * aquireHeightmap method was called and it had time to load and there is still
     * one user using it  */
    fun getHeightmapWorldCoordinates(worldX: Int, worldZ: Int): Heightmap?

    /** Returns either null or a valid, entirely loaded heightmap if the
     * aquireHeightmap method was called and it had time to load and there is still
     * one user using it  */
    fun getHeightmapLocation(location: Location): Heightmap?

    fun getHeightAtWorldCoordinates(worldX: Int, worldZ: Int): Int

    fun getTopCellAtWorldCoordinates(worldX: Int, worldZ: Int): Cell
}
