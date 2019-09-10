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
import xyz.chunkstories.api.world.cell.Cell

/** Represents the fact this entity has a concept of "head" and direction looking at */
abstract class TraitSight(entity: Entity) : Trait(entity) {
    override val traitName = "sight"

    abstract val headLocation: Location
    abstract val lookingAt: Vector3dc

    fun getLookingAt(reach: Double): RayResult {
        val entityMask = { other: Entity -> other != entity && other.traits[TraitCollidable::class]?.selectable != false}
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { !it.voxel.isAir() && (!it.voxel.liquid) }, entityMask)
        return query.trace()
    }

    fun getSelectableBlockLookingAt(reach: Double) : Cell? {
        val entityMask = { other: Entity -> other != entity && other.traits[TraitCollidable::class]?.selectable != false}
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { !it.voxel.isAir() && (!it.voxel.liquid) }, entityMask)
        return when(val hit = query.trace()) {
            is RayResult.Hit.VoxelHit -> hit.cell
            else -> null
        }
    }

    fun getSolidBlockLookingAt(reach: Double) : Cell? {
        val entityMask = { other: Entity -> other != entity && other.traits[TraitCollidable::class]?.selectable != false}
        val query = RayQuery(headLocation, lookingAt, 0.0, reach, { it.voxel.solid }, entityMask)
        return when(val hit = query.trace()) {
            is RayResult.Hit.VoxelHit -> hit.cell
            else -> null
        }
    }
}
