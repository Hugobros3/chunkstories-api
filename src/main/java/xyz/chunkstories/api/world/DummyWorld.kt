//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.slf4j.Logger
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.block.structures.Prefab
import xyz.chunkstories.api.entity.EntityID
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.CellData
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
    override val logger: Logger
        get() = TODO("Not yet implemented")
    override var properties: World.Properties = TODO()
        get() = TODO("Not yet implemented")
    override var sky: World.Sky
        get() = TODO("Not yet implemented")

    override// TODO Auto-generated method stub
    val generator: WorldGenerator
        get() = throw NotImplementedError()
    override val gameInstance: GameInstance
        get() = TODO("Not yet implemented")


    override// TODO Auto-generated method stub
    val entities: Sequence<Entity>
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


    override fun addEntity(entity: Entity): EntityID {
        TODO("Not yet implemented")
    }

    override fun removeEntity(id: EntityID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCell(x: Int, y: Int, z: Int): WorldCell? {
        TODO("Not yet implemented")
    }

    override fun getCellMut(x: Int, y: Int, z: Int): MutableWorldCell? {
        TODO("Not yet implemented")
    }

    override fun setCellData(x: Int, y: Int, z: Int, data: CellData): Boolean {
        TODO("Not yet implemented")
    }

    override fun pastePrefab(x: Int, y: Int, z: Int, prefab: Prefab): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEntity(uuid: Long): Entity? {
        // TODO Auto-generated method stub
        return throw NotImplementedError()
    }

    override fun getEntitiesInBox(box: Box): Sequence<Entity> {
        TODO("Not yet implemented")
    }

    override fun getCellsInBox(box: Box): Sequence<Cell> {
        TODO("Not yet implemented")
    }

    companion object {
        val instance: DummyWorld by lazy { DummyWorld() }
        fun get(): DummyWorld = instance
    }
}
