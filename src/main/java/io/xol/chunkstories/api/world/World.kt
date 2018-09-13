//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.GameLogic
import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.ContentTranslator
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.events.voxel.WorldModificationCause
import io.xol.chunkstories.api.exceptions.world.WorldException
import io.xol.chunkstories.api.graphics.systems.dispatching.DecalsManager
import io.xol.chunkstories.api.input.Input
import io.xol.chunkstories.api.particles.ParticlesManager
import io.xol.chunkstories.api.physics.Box
import io.xol.chunkstories.api.sound.SoundManager
import io.xol.chunkstories.api.util.IterableIterator
import io.xol.chunkstories.api.voxel.Voxel
import io.xol.chunkstories.api.voxel.VoxelFormat
import io.xol.chunkstories.api.world.cell.CellData
import io.xol.chunkstories.api.world.cell.EditableCell
import io.xol.chunkstories.api.world.cell.FutureCell
import io.xol.chunkstories.api.world.chunk.Chunk
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell
import io.xol.chunkstories.api.world.chunk.ChunkHolder
import io.xol.chunkstories.api.world.chunk.ChunksIterator
import io.xol.chunkstories.api.world.generator.WorldGenerator
import io.xol.chunkstories.api.world.heightmap.WorldHeightmaps
import io.xol.chunkstories.api.world.region.Region
import org.joml.Vector3dc

interface World {
    val worldInfo: WorldInfo

    /** Returns the map generator used to create the new bits of the map */
    val generator: WorldGenerator

    /** Returns the GameLogic thread this world runs on */
    val gameLogic: GameLogic

    /** Returns the GameContext this world lives in */
    val gameContext: GameContext

    /** Return the Content used with this world */
    val content: Content

    /** Returns the ContentTranslator associated with this world  */
    val contentTranslator: ContentTranslator

    /** @return The height of the world, the last block can be placed at maxHeight - 1 */
    val maxHeight: Int
        get() = worldInfo.size.heightInChunks * 32

    /** @return The length of a horizontal side of the world, in chunk size units (32) */
    val sizeInChunks: Int
        get() = worldInfo.size.sizeInChunks

    /** @return The length of a horizontal side of the world. */
    val worldSize: Double
        get() = (sizeInChunks * 32).toDouble()

    var defaultSpawnLocation: Location

    /** Sets the time of the World. By default the time is set at 5000 and it uses a
     * 10.000 cycle, 0 being midnight and 5000 being midday */
    var time: Long

    /** The weather is represented by a normalised float value
     * - 0.0 equals dead dry
     * - 0.2 equals sunny
     * - 0.4 equals overcast
     * - 0.5 equals foggy/cloudy
     * - above 0.5 it rains
     * - 0.8 max rain intensity
     * - 0.9 lightning
     * - 1.0 hurricane
     * */
    var weather: Float

    /** How many ticks have elapsed since the creation of this world.
     * Never resets, never skips, no matter the daytime changes. */
    val ticksElapsed: Long

    val decalsManager: DecalsManager
    val particlesManager: ParticlesManager
    val soundManager: SoundManager
    val collisionsManager: WorldCollisionsManager

    /* Entity management */

    /** Adds an entity to the world, the entity location has to be in this world !*/
    fun addEntity(entity: Entity)

    /** Removes an entity from the world. Returns true on success */
    fun removeEntity(entity: Entity): Boolean

    /** Removes an entity from the world, based on UUID */
    fun removeEntityByUUID(uuid: Long): Boolean

    /** @param uuid a valid UUID
     * @return null if it can't be found */
    fun getEntityByUUID(uuid: Long): Entity?

    /** Returns an iterator containing all the entities from within the box defined
     * by center and boxSize  */
    fun getEntitiesInBox(center: Vector3dc, boxSize: Vector3dc): NearEntitiesIterator

    /** Returns an iterator containing all the loaded entities. Supposedly
     * thread-safe  */
    val allLoadedEntities: IterableIterator<Entity>

    interface NearEntitiesIterator : IterableIterator<Entity> {
        /** Returns the distance of the last entity returned by next() to the center of
         * the box  */
        fun distance(): Double
    }

    /* Direct voxel data accessors */

    interface WorldCell : EditableCell {
        override fun getWorld(): World
    }

    /** Get the data contained in this cell as full 32-bit data format ( see
     * [VoxelFormat])
     *
     * @return the data contained in this chunk as full 32-bit data format ( see
     * [VoxelFormat])
     * @throws WorldException if it couldn't peek the world at the specified
     * location for some reason
     */
    @Throws(WorldException::class)
    fun peek(x: Int, y: Int, z: Int): ChunkCell

    /** Convenient overload of peek() to take a Vector3dc derivative ( ie: a
     * Location object )  */
    @Throws(WorldException::class)
    fun peek(location: Vector3dc): ChunkCell

    /** Safely calls peek() and returns a WorldVoxelContext no matter what.
     * Zeroes-out if the normal peek() would have failed.  */
    fun peekSafely(x: Int, y: Int, z: Int): WorldCell

    /** Convenient overload of peekSafely() to take a Vector3dc derivative ( ie: a
     * Location object )  */
    fun peekSafely(location: Vector3dc): WorldCell

    /** Alternative to peek() that does not newEntity any VoxelContext object<br></br>
     * **Does not throw exceptions**, instead safely returns zero upon
     * failure.  */
    fun peekSimple(x: Int, y: Int, z: Int): Voxel

    /** Peek the raw data of the chunk  */
    fun peekRaw(x: Int, y: Int, z: Int): Int

    /** Poke new information in a voxel cell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * It will also trigger lightning and such updates
     *
     * @throws WorldException if it couldn't poke the world at the specified
     * location, for example if it's not loaded
     */
    @Throws(WorldException::class)
    fun poke(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int, cause: WorldModificationCause?): WorldCell

    /** Simply use a FutureVoxelContext to ease modifications  */
    @Throws(WorldException::class)
    fun poke(fvc: FutureCell, cause: WorldModificationCause?): CellData

    /** Poke new information in a voxel cell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * It will also trigger lightning and such updates  */
    fun pokeSimple(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    fun pokeSimple(fvc: FutureCell)

    /** Poke new information in a voxel cell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * This will *not* trigger any update.  */
    fun pokeSimpleSilently(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    fun pokeSimpleSilently(fvc: FutureCell)

    /** Poke the raw data for a voxel cell Takes a full 32-bit data format ( see
     * [VoxelFormat])  */
    fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int)

    /** Poke the raw data for a voxel cell Takes a full 32-bit data format ( see
     * [VoxelFormat]) Does not trigger any updates.  */
    fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int)

    fun getVoxelsWithin(boundingBox: Box): IterableIterator<CellData>

    /* Chunks */

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolderLocation(user: WorldUser, location: Location): ChunkHolder?

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolderWorldCoordinates(user: WorldUser, worldX: Int, worldY: Int, worldZ: Int): ChunkHolder?

    /** acquires a ChunkHolder and registers it's user, triggering a load operation
     * for the underlying chunk and preventing it to unload until all the users
     * either unregisters or gets garbage collected and it's reference nulls out.  */
    fun acquireChunkHolder(user: WorldUser, chunkX: Int, chunkY: Int, chunkZ: Int): ChunkHolder?

    /** Returns true if a chunk was loaded. Not recommanded nor intended to use as a
     * replacement for a '== null' check after getChunk() because of the load/unload
     * mechanisms !  */
    fun isChunkLoaded(chunkX: Int, chunkY: Int, chunkZ: Int): Boolean

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk?

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunkWorldCoordinates(worldX: Int, worldY: Int, worldZ: Int): Chunk?

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    fun getChunkWorldCoordinates(location: Location): Chunk?

    /** Returns either null or a valid chunk if a corresponding ChunkHolder was
     * acquired by someone and the chunk had time to load.  */
    val allLoadedChunks: ChunksIterator

    /* Regions */

    //TODO holder for regions and move aquire() stuff there

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

    val regionsSummariesHolder: WorldHeightmaps

    /** Called when some controllable entity try to interact with the world
     *
     * @return true if the interaction was handled
     */
    fun handleInteraction(entity: Entity, blockLocation: Location, input: Input): Boolean
}