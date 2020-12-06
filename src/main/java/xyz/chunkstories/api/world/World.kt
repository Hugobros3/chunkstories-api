//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.joml.Vector3d
import org.slf4j.Logger
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.util.IterableIterator
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.generator.WorldGenerator
import xyz.chunkstories.api.world.heightmap.WorldHeightmapsManager
import xyz.chunkstories.api.entity.EntityID
import xyz.chunkstories.api.voxel.structures.Prefab
import xyz.chunkstories.api.world.cell.MutableCell
import xyz.chunkstories.api.world.chunk.WorldChunksManager
import xyz.chunkstories.api.world.region.WorldRegionsManager

interface World {
    val gameInstance: GameInstance

    var properties: Properties
    data class Properties(
            val internalName: String,
            /** Display name */
            val name: String,
            val description: String,
            val seed: String,
            val size: WorldSize,
            val spawn: Vector3d,
            val generator: String
            )

    val generator: WorldGenerator

    var sky: Sky
    data class Sky(
            val timeOfDay: Float,
            val overcast: Float,
            val raining: Float
    )

    val ticksElapsed: Long

    /** Adds an entity to the world, the entity location has to be in this world !*/
    fun addEntity(entity: Entity): EntityID
    fun getEntity(id: EntityID): Entity?
    fun getEntitiesInBox(box: Box): Sequence<Entity>
    val entities: Sequence<Entity>
    fun removeEntity(id: EntityID): Boolean

    fun getCell(x: Int, y: Int, z: Int): Cell?
    fun getCellMut(x: Int, y: Int, z: Int): MutableCell?

    fun pastePrefab(x: Int, y: Int, z: Int, prefab: Prefab)

   /* /** Get the data contained in this cell
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
    fun peekSimple(x: Int, y: Int, z: Int): Voxel*/

    /** Peek the raw data of the chunk  */
    fun peekRaw(x: Int, y: Int, z: Int): Int

    /*/** Poke new information in a voxel getCell.
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

    fun pokeSimpleSilently(fvc: FutureCell)*/

    /** Poke the raw data for a voxel getCell Takes a full 32-bit data format ( see [VoxelFormat])  */
    fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int)

    /** Poke the raw data for a voxel getCell Takes a full 32-bit data format ( see
     * [VoxelFormat]) Does not trigger any updates.  */
    fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int)

    fun getVoxelsWithin(boundingBox: Box): IterableIterator<Cell>

    val decalsManager: DecalsManager
    val particlesManager: ParticlesManager
    val soundManager: SoundManager
    val collisionsManager: WorldCollisionsManager

    val chunksManager: WorldChunksManager
    val regionsManager: WorldRegionsManager
    val heightmapsManager: WorldHeightmapsManager

    val logger: Logger
}

// Helper function
// TODO move somewhere more appropriate
val World.animationTime: Float
    get() {
        val realWorldTimeTruncated = (System.nanoTime() % 1000_000_000_000)
        val realWorldTimeMs = realWorldTimeTruncated / 1000_000
        return (realWorldTimeMs / 1000.0f) * 1000.0f
    }