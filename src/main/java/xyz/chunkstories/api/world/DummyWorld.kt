//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.joml.Vector3dc
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.util.IterableIterator
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.chunk.ChunkCell
import xyz.chunkstories.api.world.chunk.WorldChunksManager
import xyz.chunkstories.api.world.generator.WorldGenerator
import xyz.chunkstories.api.world.heightmap.WorldHeightmapsManager
import xyz.chunkstories.api.world.region.WorldRegionsManager

/** DummyWorld doesn't exist. DummyWorld is immutable. DummyWorld is unique. You
 * can't prove the existence of the DummyWorld, but neither can you disprove it.
 * Don't mess with the forces at play here. You have been warned.  */
class DummyWorld : World {

    override val chunksManager: WorldChunksManager
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val regionsManager: WorldRegionsManager
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val heightmapsManager: WorldHeightmapsManager
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val properties: World.Properties
        get() = TODO("Not yet implemented")
    override val sky: World.Sky
        get() = TODO("Not yet implemented")

    override// TODO Auto-generated method stub
    val generator: WorldGenerator
        get() = throw NotImplementedError()
    override val gameInstance: GameInstance
        get() = TODO("Not yet implemented")


    override// TODO Auto-generated method stub
    val entities: IterableIterator<Entity>
        get() = throw NotImplementedError()

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

    override val collisionsManager: WorldCollisionsManager
        get() = throw NotImplementedError()


    override fun addEntity(entity: Entity) {
        // TODO Auto-generated method stub

    }

    override fun removeEntity(entity: Entity): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun removeEntity(id: EntityID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEntity(uuid: Long): Entity? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getEntitiesInBox(box: Box): World.NearEntitiesIterator {
        TODO("Not yet implemented")
    }

    @Throws(WorldException::class)
    override fun tryPeek(x: Int, y: Int, z: Int): ChunkCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    @Throws(WorldException::class)
    override fun tryPeek(location: Vector3dc): ChunkCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peek(x: Int, y: Int, z: Int): WorldCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peek(location: Vector3dc): WorldCell {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peekSimple(x: Int, y: Int, z: Int): Voxel {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getVoxelsWithin(boundingBox: Box): IterableIterator<Cell> {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun peekRaw(x: Int, y: Int, z: Int): Int {
        // TODO Auto-generated method stub
        return 0
    }

    @Throws(WorldException::class)
    override fun poke(x: Int, y: Int, z: Int, voxel: Voxel?, sunlight: Int, blocklight: Int, metadata: Int, cause: WorldModificationCause?): WorldCell {
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
    override fun poke(fvc: FutureCell, cause: WorldModificationCause?): Cell {
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
