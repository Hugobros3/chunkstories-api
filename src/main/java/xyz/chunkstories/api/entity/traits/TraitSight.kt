//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import org.joml.Vector3dc
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.physics.RayQuery
import xyz.chunkstories.api.physics.RayResult
import xyz.chunkstories.api.physics.trace
import xyz.chunkstories.api.util.kotlin.toVec3i
import xyz.chunkstories.api.world.cell.Cell

/** Represents the fact this entity has a concept of "head" and direction looking at */
abstract class TraitSight(entity: Entity) : Trait(entity) {

    abstract val headLocation: Location
    abstract val lookingAt: Vector3dc

    fun getLookingAt(reach: Double): RayResult {
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { !it.voxel.isAir() && (!it.voxel.liquid) })
        return query.trace()
    }

    fun getSelectableBlockLookingAt(reach: Double) : Cell? {
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { !it.voxel.isAir() && (!it.voxel.liquid) })
        return when(val hit = query.trace()) {
            is RayResult.Hit.VoxelHit -> hit.cell
            else -> null
        }
    }

    fun getSolidBlockLookingAt(reach: Double) : Cell? {
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { it.voxel.solid })
        return when(val hit = query.trace()) {
            is RayResult.Hit.VoxelHit -> hit.cell
            else -> null
        }
    }

    fun getFreeSpaceAdjacentToSolidBlock(reach: Double) : Cell? {
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { it.voxel.solid })
        when(val hit = query.trace()) {
            is RayResult.Hit.VoxelHit -> {
                val normali = hit.normal.toVec3i()
                val x = hit.cell.x + normali.x
                val y = hit.cell.y + normali.y
                val z = hit.cell.z + normali.z
                if(y in 0 until hit.cell.world.maxHeight) {
                    return hit.cell.world.peek(x, y, z)
                }
            }
        }

        return null
    }
}
