//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.region

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldUser

interface WorldRegionsManager {
    val world: World

    val allLoadedRegions: Sequence  <Region>

    /** acquires a region and registers it's user, triggering a load operation for
     * the region and preventing it to unload until all the users either unregisters
     * or gets garbage collected and it's reference nulls out.  */
    fun acquireRegion(user: WorldUser, regionX: Int, regionY: Int, regionZ: Int): Region?

    /** acquires a region and registers it's user, triggering a load operation for
     * the region and preventing it to unload until all the users either unregisters
     * or gets garbage collected and it's reference nulls out.  */
    fun acquireRegionChunkCoordinates(user: WorldUser, chunkX: Int, chunkY: Int, chunkZ: Int): Region?

    /** acquires a region and registers it's user, triggering a load operation for
     * the region and preventing it to unload until all the users either unregisters
     * or gets garbage collected and it's reference nulls out.  */
    fun acquireRegionWorldCoordinates(user: WorldUser, worldX: Int, worldY: Int, worldZ: Int): Region?

    /** acquires a region and registers it's user, triggering a load operation for
     * the region and preventing it to unload until all the users either unregisters
     * or gets garbage collected and it's reference nulls out.  */
    fun acquireRegionLocation(user: WorldUser, location: Location): Region?

    /** Returns either null or a valid, entirely loaded region if the acquireRegion
     * method was called and it had time to load and there is still one user using
     * it  */
    fun getRegion(regionX: Int, regionY: Int, regionZ: Int): Region?

    /** Returns either null or a valid, entirely loaded region if the acquireRegion
     * method was called and it had time to load and there is still one user using
     * it  */
    fun getRegionChunkCoordinates(chunkX: Int, chunkY: Int, chunkZ: Int): Region?

    /** Returns either null or a valid, entirely loaded region if the acquireRegion
     * method was called and it had time to load and there is still one user using
     * it  */
    fun getRegionWorldCoordinates(worldX: Int, worldY: Int, worldZ: Int): Region?

    /** Returns either null or a valid, entirely loaded region if the acquireRegion
     * method was called and it had time to load and there is still one user using
     * it  */
    fun getRegionLocation(location: Location): Region?
}