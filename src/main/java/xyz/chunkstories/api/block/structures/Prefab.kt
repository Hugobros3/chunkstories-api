//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.block.structures

import org.joml.Vector3i
import xyz.chunkstories.api.world.cell.*
import xyz.chunkstories.api.world.chunk.Chunk

class PrefabCell(x: Int, y: Int, z: Int, data: CellData) : PodCell(x, y, z, data)

open class Prefab(val size: Vector3i, protected val offset: Vector3i, protected val cells: Array<PrefabCell>)

abstract class CustomPrefab(size: Vector3i, offset: Vector3i) : Prefab(size, offset, emptyArray()) {
    abstract fun paste(chunk: Chunk, position: Vector3i, flags: PrefabPasteFlags = PrefabPasteFlags())
}

data class PrefabPasteFlags(val overwriteAir: Boolean = false, val ignoreOffset: Boolean = false)

//TODO move to implementation
    /*fun paste(world: World, position: Vector3i?, flags: Int) {
        val actualPosition = Vector3i(position)
        if (flags and FLAG_USE_OFFSET != 0) actualPosition.add(offset)

        val air = world.gameInstance.content.voxels.air
        for (x in 0 until size.x) {
            for (y in 0 until size.y) {
                for (z in 0 until size.z) {
                    /*val future = PrefabCell(actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, data)
                    future.voxel = cells[x + y * size.x + z * size.x * size.y].data.voxel
                    // world.poke(future, null);
                    if (!future.voxel.isAir() || flags and FLAG_DONT_OVERWRITE_AIR == 0) world.pokeSimple(future)*/
                    world.peekMut(x, y, z)
                }
            }
        }
    }

    fun paste(chunk: Chunk, position: Vector3i?, flags: Int) {
        val actualPosition = Vector3i(position)
        if (flags and FLAG_USE_OFFSET != 0) actualPosition.add(offset)

        val air: Voxel = chunk.world.gameInstance.content.voxels.air
        val initX = Math.max(0, chunk.chunkX * 32 - actualPosition.x)
        val boundX = Math.min(size.x, (chunk.chunkX + 1) * 32 - actualPosition.x)
        val initY = Math.max(0, chunk.chunkY * 32 - actualPosition.y)
        val boundY = Math.min(size.y, (chunk.chunkY + 1) * 32 - actualPosition.y)
        val initZ = Math.max(0, chunk.chunkZ * 32 - actualPosition.z)
        val boundZ = Math.min(size.z, (chunk.chunkZ + 1) * 32 - actualPosition.z)
        for (x in initX until boundX) {
            for (y in initY until boundY) {
                for (z in initZ until boundZ) {
                    val future = FutureCell(chunk.world, actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, air)
                    future.voxel = data[x + y * size.x + z * size.x * size.y].voxel
                    if (!future.voxel.isAir() || flags and FLAG_DONT_OVERWRITE_AIR == 0) chunk.pokeSimpleSilently(actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, future.voxel, future.metaData, 0,
                            0)
                }
            }
        }
        chunk.lightBaker.requestUpdate()
        chunk.occlusion.requestUpdate()
        chunk.mesh.requestUpdate()
    }

    companion object {
        const val FLAG_DONT_OVERWRITE_AIR = 0x1
        const val FLAG_USE_OFFSET = 0x2
    }*/
