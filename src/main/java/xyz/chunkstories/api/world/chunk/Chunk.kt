//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk

import org.joml.Vector3dc
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.cell.CellComponents
import xyz.chunkstories.api.world.region.Region

/** Contains 32x32x32 voxels worth of data  */
interface Chunk {
    val world: World

    val region: Region

    val chunkX: Int
    val chunkY: Int
    val chunkZ: Int

    val isAirChunk: Boolean

    /** Add some entity to the chunk's confines  */
    fun addEntity(entity: Entity)

    /** Remove some entity from the chunk's confines  */
    fun removeEntity(entity: Entity)

    val entitiesWithinChunk: Collection<Entity>

    val occlusion: ChunkOcclusionManager

    /** Returns the interface responsible of updating the voxel light of this
     * chunk  */
    val lightBaker: ChunkLightUpdater

    val mesh: ChunkMesh

    val holder: ChunkHolder

    /** Get the data contained in this chunk as full 32-bit data format ( see
     * [VoxelFormat]) The coordinates are internally modified to map to the
     * chunk, meaning you can access it both with world coordinates or 0-31 in-chunk
     * coordinates Just don't give it negatives
     *
     * @return the data contained in this chunk as full 32-bit data format ( see
     * [VoxelFormat])
     * @throws WorldException if it couldn't peek the world at the specified
     * location for some reason
     */
    fun peek(x: Int, y: Int, z: Int): ChunkCell

    /** Convenient overload of peek() to take a Vector3dc derivative ( ie: a
     * Location object )  */
    fun peek(location: Vector3dc): ChunkCell

    /** Alternative to peek() that does not newEntity any VoxelContext object<br></br>
     * **Does not throw exceptions**, instead safely returns zero upon
     * failure.  */
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
     * @throws WorldException if it couldn't poke the world at the specified location for some reason
     */
    @Throws(WorldException::class)
    fun poke(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int, cause: WorldModificationCause?): ChunkCell

    /** Poke new information in a voxel getCell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.
     *
     * It will also trigger lightning and such updates  */
    fun pokeSimple(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    /** Poke new information in a voxel getCell.
     *
     * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
     * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
     * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
     * updated.  */
    fun pokeSimpleSilently(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int)

    /** Does the same as [.poke] but without creating any VoxelContext object
     * or triggering any updates<br></br>
     * **Does not throw exceptions**, instead fails silently.  */
    fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int)

    /** Does the same as [.poke] but without creating any VoxelContext object
     * or triggering any updates<br></br>
     * **Does not throw exceptions**, instead fails silently.  */
    fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int)

    /** Obtains the EntityVoxel saved at the given location  */
    fun getComponentsAt(worldX: Int, worldY: Int, worldZ: Int): CellComponents

    fun destroy()

}