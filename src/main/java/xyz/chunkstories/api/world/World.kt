//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.joml.Vector3d
import org.joml.Vector3dc
import org.slf4j.Logger
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.block.BlockAdditionalData
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.generator.WorldGenerator
import xyz.chunkstories.api.world.heightmap.WorldHeightmapsManager
import xyz.chunkstories.api.entity.EntityID
import xyz.chunkstories.api.block.structures.Prefab
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.net.PacketWorld
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.server.Host
import xyz.chunkstories.api.world.cell.CellData
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
            val timeOfDay: Float = 0.0f,
            val overcast: Float = 0.0f,
            val raining: Float = 0.0f
    )

    val ticksElapsed: Long

    /** Adds an entity to the world, the entity location has to be in this world !*/
    fun addEntity(entity: Entity): EntityID
    fun getEntity(id: EntityID): Entity?
    fun getEntitiesInBox(box: Box): Sequence<Entity>
    val entities: Sequence<Entity>
    fun removeEntity(id: EntityID): Boolean

    fun getCell(x: Int, y: Int, z: Int): WorldCell?
    fun getCellMut(x: Int, y: Int, z: Int): MutableWorldCell?

    fun setCellData(x: Int, y: Int, z: Int, data: CellData): Boolean
    fun pastePrefab(x: Int, y: Int, z: Int, prefab: Prefab): Boolean

    fun getCellsInBox(box: Box): Sequence<Cell>

    val decalsManager: DecalsManager
    val particlesManager: ParticlesManager
    val soundManager: SoundManager
    val collisionsManager: WorldCollisionsManager

    val chunksManager: WorldChunksManager
    val regionsManager: WorldRegionsManager
    val heightmapsManager: WorldHeightmapsManager

    val logger: Logger
}

fun World.getCell(location: Location): WorldCell? { assert(location.world == this) ; return getCell(location.x.toInt(), location.y.toInt(), location.z.toInt()) }
fun World.getCellMut(location: Location): MutableWorldCell? { assert(location.world == this) ; return getCellMut(location.x.toInt(), location.y.toInt(), location.z.toInt()) }

fun World.getCell(location: Vector3dc) = getCell(location.x().toInt(), location.y().toInt(), location.z().toInt())
fun World.getCellMut(location: Vector3dc) = getCellMut(location.x().toInt(), location.y().toInt(), location.z().toInt())

// Helper function
// TODO move somewhere more appropriate
val World.animationTime: Float
    get() {
        val realWorldTimeTruncated = (System.nanoTime() % 1000_000_000_000)
        val realWorldTimeMs = realWorldTimeTruncated / 1000_000
        return (realWorldTimeMs / 1000.0f) * 1000.0f
    }

/** A 'master' world is one hosting the game logic and who runs the 'serverside'
 * plugins. It can be either a dedicated server or a singleplayer world. */
interface WorldMaster : World {
    override val gameInstance: Host

    /** Returns the folder where the world files are on disk.  */
    val folderPath: String

    fun BlockAdditionalData.pushChanges()
    fun Player.startPlayingAs(entity: Entity)
    fun Player.startSpectating()

    fun Player.pushPacket(packet: PacketWorld)

    val players: Sequence<Player>
}

interface WorldSub : World {
    val remoteServer: Subscriber

    fun pushPacket(packet: Packet)
}