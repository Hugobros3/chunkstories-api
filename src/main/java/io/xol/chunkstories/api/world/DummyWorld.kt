//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world

import io.xol.chunkstories.api.graphics.systems.dispatching.DecalsManager
import org.joml.Vector3dc

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.GameLogic
import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.ContentTranslator
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.events.voxel.WorldModificationCause
import io.xol.chunkstories.api.exceptions.world.WorldException
import io.xol.chunkstories.api.input.Input
import io.xol.chunkstories.api.particles.ParticlesManager
import io.xol.chunkstories.api.physics.Box
import io.xol.chunkstories.api.sound.SoundManager
import io.xol.chunkstories.api.util.IterableIterator
import io.xol.chunkstories.api.voxel.Voxel
import io.xol.chunkstories.api.world.cell.CellData
import io.xol.chunkstories.api.world.cell.FutureCell
import io.xol.chunkstories.api.world.chunk.Chunk
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell
import io.xol.chunkstories.api.world.chunk.ChunkHolder
import io.xol.chunkstories.api.world.chunk.ChunksIterator
import io.xol.chunkstories.api.world.generator.WorldGenerator
import io.xol.chunkstories.api.world.heightmap.WorldHeightmaps
import io.xol.chunkstories.api.world.region.Region

/** DummyWorld doesn't exist. DummyWorld is immutable. DummyWorld is unique. You
 * can't prove the existence of the DummyWorld, but neither can you disprove it.
 * Don't mess with the forces at play here. You have been warned.  */
class DummyWorld : World {

    override// TODO Auto-generated method stub
    val worldInfo: WorldInfo
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val generator: WorldGenerator
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val gameLogic: GameLogic
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val gameContext: GameContext
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val allLoadedEntities: IterableIterator<Entity>
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val allLoadedChunks: Set<Chunk>
        get() = emptySet()

    override// TODO Auto-generated method stub
    val regionsSummariesHolder: WorldHeightmaps
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    // TODO Auto-generated method stub
    var defaultSpawnLocation: Location
        get() = throw NotImplementedError()
        set(location) {

        }

    override// TODO Auto-generated method stub
    // TODO Auto-generated method stub
    var time: Long
        get() = 0
        set(time) {

        }

    override// TODO Auto-generated method stub
    // TODO Auto-generated method stub
    var weather: Float
        get() = 0f
        set(overcastFactor) {

        }

    override// TODO Auto-generated method stub
    val ticksElapsed: Long
        get() = 0

    override// TODO Auto-generated method stub
    val decalsManager: DecalsManager
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val particlesManager: ParticlesManager
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val soundManager: SoundManager
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val contentTranslator: ContentTranslator
        get() = throw NotImplementedError()

    override// TODO Auto-generated method stub
    val content: Content
        get() = throw NotImplementedError()

    override val collisionsManager: WorldCollisionsManager
        get() = throw NotImplementedError()

    override val maxHeight: Int
        get() = 0

    override val sizeInChunks: Int
        get() = 0

    override val worldSize: Double
        get() = 0.0

    override fun addEntity(entity: Entity) {
        // TODO Auto-generated method stub

    }

    override fun removeEntity(entity: Entity): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun removeEntityByUUID(uuid: Long): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun getEntityByUUID(uuid: Long): Entity? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getEntitiesInBox(center: Vector3dc, boxSize: Vector3dc): World.NearEntitiesIterator {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    @Throws(WorldException::class)
    override fun peek(x: Int, y: Int, z: Int): ChunkCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    @Throws(WorldException::class)
    override fun peek(location: Vector3dc): ChunkCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peekSafely(x: Int, y: Int, z: Int): World.WorldCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peekSafely(location: Vector3dc): World.WorldCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peekSimple(x: Int, y: Int, z: Int): Voxel {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getVoxelsWithin(boundingBox: Box): IterableIterator<CellData> {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireChunkHolderLocation(user: WorldUser, location: Location): ChunkHolder? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireChunkHolderWorldCoordinates(user: WorldUser, worldX: Int, worldY: Int, worldZ: Int): ChunkHolder? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireChunkHolder(user: WorldUser, chunkX: Int, chunkY: Int, chunkZ: Int): ChunkHolder? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun isChunkLoaded(chunkX: Int, chunkY: Int, chunkZ: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun getChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getChunkWorldCoordinates(worldX: Int, worldY: Int, worldZ: Int): Chunk? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getChunkWorldCoordinates(location: Location): Chunk? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireRegion(user: WorldUser, regionX: Int, regionY: Int, regionZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireRegionChunkCoordinates(user: WorldUser, chunkX: Int, chunkY: Int, chunkZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireRegionWorldCoordinates(user: WorldUser, worldX: Int, worldY: Int, worldZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun acquireRegionLocation(user: WorldUser, location: Location): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getRegion(regionX: Int, regionY: Int, regionZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getRegionChunkCoordinates(chunkX: Int, chunkY: Int, chunkZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getRegionWorldCoordinates(worldX: Int, worldY: Int, worldZ: Int): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getRegionLocation(location: Location): Region? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun handleInteraction(entity: Entity, blockLocation: Location?, input: Input): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun peekRaw(x: Int, y: Int, z: Int): Int {
        // TODO Auto-generated method stub
        return 0
    }

    @Throws(WorldException::class)
    override fun poke(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int, cause: WorldModificationCause?): World.WorldCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun pokeSimpleSilently(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int) {
        // TODO Auto-generated method stub

    }

    override fun pokeRaw(x: Int, y: Int, z: Int, newVoxelData: Int) {
        // TODO Auto-generated method stub

    }

    override fun pokeRawSilently(x: Int, y: Int, z: Int, newVoxelData: Int) {
        // TODO Auto-generated method stub

    }

    @Throws(WorldException::class)
    override fun poke(fvc: FutureCell, cause: WorldModificationCause?): CellData {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun pokeSimple(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int) {
        // TODO Auto-generated method stub

    }

    override fun pokeSimple(fvc: FutureCell) {
        // TODO Auto-generated method stub

    }

    override fun pokeSimpleSilently(fvc: FutureCell) {
        // TODO Auto-generated method stub

    }

    companion object {
        val instance: DummyWorld by lazy { DummyWorld() }
        fun get(): DummyWorld = instance
    }
}
