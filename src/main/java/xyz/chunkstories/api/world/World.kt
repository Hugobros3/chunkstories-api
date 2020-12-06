//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.GameLogic
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.util.IterableIterator
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.FutureCell
import xyz.chunkstories.api.world.chunk.ChunkCell
import xyz.chunkstories.api.world.generator.WorldGenerator
import xyz.chunkstories.api.world.heightmap.WorldHeightmapsManager
import org.joml.Vector3dc
import xyz.chunkstories.api.world.chunk.WorldChunksManager
import xyz.chunkstories.api.world.region.WorldRegionsManager

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
    @Deprecated("TODO remove", ReplaceWith("worldInfo.size.heightInChunks * 32"))
    val maxHeight: Int
        get() = worldInfo.size.heightInChunks * 32

    /** @return The length of a horizontal side of the world, in chunk size units (32) */
    @Deprecated("TODO remove", ReplaceWith("worldInfo.size.sizeInChunks"))
    val sizeInChunks: Int
        get() = worldInfo.size.sizeInChunks

    /** @return The length of a horizontal side of the world. */
    @Deprecated("TODO remove", ReplaceWith("(sizeInChunks * 32).toDouble()"))
    val worldSize: Double
        get() = (sizeInChunks * 32).toDouble()

    var defaultSpawnLocation: Location

    /** The position of the sun in the sky. [0-24000[ */
    var sunCycle: Int

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

    /** Returns an iterator containing all the entities from within the box  */
    fun getEntitiesInBox(box: Box): NearEntitiesIterator

    /** Returns an iterator containing all the loaded entities. Supposedly thread-safe */
    val allLoadedEntities: IterableIterator<Entity>

    interface NearEntitiesIterator : IterableIterator<Entity> {
        /** Returns the distance of the last entity returned by next() to the center of the box */
        fun distance(): Double
    }

    /* Direct voxel data accessors */

    /** Get the data contained in this cell
     * @throws WorldException if it couldn't peek the world at the specified
     * location for some reason
     */
    @Throws(WorldException::class)
    fun tryPeek(x: Int, y: Int, z: Int): ChunkCell

    /** Convenient overload of tryPeek() to take a vector */
    @Throws(WorldException::class)
    fun tryPeek(location: Vector3dc): ChunkCell

    /** Safely calls peeks and returns a WorldVoxelContext no matter what.
     * Zeroes-out if the normal peek() would have failed.  */
    fun peek(x: Int, y: Int, z: Int): WorldCell

    /** Convenient overload of peek() to take a vector */
    fun peek(location: Vector3dc): WorldCell

    /** Alternative to peek() that does not create any Cell object<br></br>
     * **Does not throw exceptions**, instead  returns zero upon failure.  */
    fun peekSimple(x: Int, y: Int, z: Int): Voxel

    /** Peek the raw data of the chunk  */
    fun peekRaw(x: Int, y: Int, z: Int): Int

    /** Poke new information in a voxel getCell.
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
    fun poke(fvc: FutureCell, cause: WorldModificationCause?): Cell

    /** Poke new information in a voxel getCell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * It will also trigger lightning and such updates  */
    fun pokeSimple(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    fun pokeSimple(fvc: FutureCell)

    /** Poke new information in a voxel getCell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * This will *not* trigger any update.  */
    fun pokeSimpleSilently(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    fun pokeSimpleSilently(fvc: FutureCell)

    /** Poke the raw data for a voxel getCell Takes a full 32-bit data format ( see [VoxelFormat])  */
    fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int)

    /** Poke the raw data for a voxel getCell Takes a full 32-bit data format ( see
     * [VoxelFormat]) Does not trigger any updates.  */
    fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int)

    fun getVoxelsWithin(boundingBox: Box): IterableIterator<Cell>

    ///** Returns either null or a valid chunk if a corresponding ChunkHolder was
    // * acquired by someone and the chunk had time to load.  */
    //val loadedChunks: Sequence<Chunk>
    //val loadedRegions: Collection<Region>
    val chunksManager: WorldChunksManager
    val regionsManager: WorldRegionsManager
    val heightmapsManager: WorldHeightmapsManager
}

val World.animationTime: Float
    get() {
        val realWorldTimeTruncated = (System.nanoTime() % 1000_000_000_000)
        val realWorldTimeMs = realWorldTimeTruncated / 1000_000
        return (realWorldTimeMs / 1000.0f) * 1000.0f
    }