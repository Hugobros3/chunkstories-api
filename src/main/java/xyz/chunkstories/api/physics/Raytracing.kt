package xyz.chunkstories.api.physics

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.TraitHitboxes
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.cell.CellData
import kotlin.math.sqrt

data class RayQuery(val origin: Location,
                    val direction: Vector3dc,
                    val tMin: Double = 0.0,
                    val tMax: Double = 256.0,
                    val voxelMask: (CellData) -> Boolean = { it.voxel.solid },
                    val entityMask: (Entity) -> Boolean = { true }) {
    val invDir = Vector3d(1.0 / direction.x(), 1.0 / direction.y(), 1.0 / direction.z())
}

sealed class RayResult(val rayQuery: RayQuery) {

    class NoHit(rayQuery: RayQuery) : RayResult(rayQuery) {
        override fun toString(): String {
            return "NoHit()"
        }
    }

    sealed class Hit(val hitPosition: Vector3dc, val t: Double, val normal: Vector3dc, rayQuery: RayQuery) : RayResult(rayQuery) {
        fun keepGoing(): RayResult {
            val newRay = RayQuery(rayQuery.origin, rayQuery.direction, t, rayQuery.tMax, rayQuery.voxelMask, rayQuery.entityMask)
            return newRay.trace()
        }

        class VoxelHit(val cell: CellData, hitPosition: Vector3dc, t: Double, normal: Vector3dc, rayQuery: RayQuery) : Hit(hitPosition, t, normal, rayQuery) {
            override fun toString(): String {
                return "VoxelHit(cell=$cell, pos: $hitPosition t=$t, normal=$normal)"
            }
        }

        class EntityHit(val entity: Entity, val part: EntityHitbox?, hitPosition: Vector3dc, t: Double, normal: Vector3dc, rayQuery: RayQuery) : Hit(hitPosition, t, normal, rayQuery) {
            override fun toString(): String {
                return "EntityHit(entity=$entity, part:$part pos: $hitPosition t=$t, normal=$normal)"
            }
        }
    }
}

fun RayQuery.trace(): RayResult {
    val direction = Vector3d(this.direction).normalize()
    val origin = this.origin
    var cell: CellData

    // DDA algorithm
    // It requires double arrays because it works using loops over each dimension
    val rayOrigin = DoubleArray(3)
    val rayDirection = DoubleArray(3)
    rayOrigin[0] = origin.x() + direction.x() * this.tMin
    rayOrigin[1] = origin.y() + direction.y() * this.tMin
    rayOrigin[2] = origin.z() + direction.z() * this.tMin
    rayDirection[0] = direction.x()
    rayDirection[1] = direction.y()
    rayDirection[2] = direction.z()
    val voxelCoords = intArrayOf(origin.x().toInt(), origin.y().toInt(), origin.z().toInt())
    val voxelDelta = intArrayOf(0, 0, 0)
    val deltaDist = DoubleArray(3)
    val next = DoubleArray(3)
    val step = IntArray(3)

    var side: Int
    // Prepare distances
    for (i in 0..2) {
        val deltaX = rayDirection[0] / rayDirection[i]
        val deltaY = rayDirection[1] / rayDirection[i]
        val deltaZ = rayDirection[2] / rayDirection[i]
        deltaDist[i] = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
        if (rayDirection[i] < 0f) {
            step[i] = -1
            next[i] = (rayOrigin[i] - voxelCoords[i]) * deltaDist[i]
        } else {
            step[i] = 1
            next[i] = (voxelCoords[i] + 1f - rayOrigin[i]) * deltaDist[i]
        }
    }

    val consideredEntities = HashSet<Entity>()
    //var chunkX = voxelCoords[0] / 32
    var chunkX = -100000
    var chunkY = voxelCoords[1] / 32
    var chunkZ = voxelCoords[2] / 32

    var tMinEntity = Double.MAX_VALUE
    var entityHit: RayResult.Hit.EntityHit? = null

    do {
        // DDA steps
        side = 0
        for (i in 1..2) {
            if (next[side] > next[i]) {
                side = i
            }
        }
        next[side] += deltaDist[side]
        voxelCoords[side] += step[side]
        voxelDelta[side] += step[side]

        val x = voxelCoords[0]
        val y = voxelCoords[1]
        val z = voxelCoords[2]

        val cx = x / 32
        val cy = y / 32
        val cz = z / 32
        // Every time we change chunk, consider potentital entities
        if (cx != chunkX || cy != chunkY || cz != chunkZ) {
            chunkX = cx
            chunkZ = cy
            chunkY = cz
            val potentialEntities = this.origin.world.getEntitiesInBox(Vector3d(cx.toDouble(), cy.toDouble(), cz.toDouble()), Vector3d(32.0, 32.0, 32.0))
            for (entity in potentialEntities) {
                // Consider each entity only once
                if (consideredEntities.add(entity)) {
                    val traitCollisions = entity.traits[TraitCollidable::class] ?: continue
                    val traitHitboxes = entity.traits[TraitHitboxes::class]
                }
            }
        }

        cell = this.origin.world.peekSafely(x, y, z)
        val voxel = cell.voxel

        if (this.voxelMask(cell)) {
            if (voxel.isAir())
                continue

            var nearest = Double.MAX_VALUE
            var normal: Vector3dc = Vector3d()
            for (box in cell.translatedCollisionBoxes ?: emptyArray()) {
                val intersection = box.intersect(origin, direction, this.invDir)
                if (intersection != null) {
                    //val distance = collisionPoint.distance(origin)
                    val distance = intersection.tMin
                    if (distance > this.tMin && distance <= this.tMax) {
                        if (distance < nearest) {
                            nearest = distance
                            normal = intersection.normal
                        }
                    }
                }
            }
            if (nearest != Double.MAX_VALUE) {
                if(nearest < tMinEntity) {
                    val tMin = nearest
                    val hitPosition = Vector3d(origin)
                    hitPosition.add(direction.x() * tMin,direction.y() * tMin,direction.z() * tMin)
                    return RayResult.Hit.VoxelHit(cell, hitPosition, tMin, normal, this)
                }
            }
        }

        // distance += deltaDist[side];

    } while (voxelDelta[0] * voxelDelta[0] + voxelDelta[1] * voxelDelta[1] + voxelDelta[2] * voxelDelta[2] < this.tMax * this.tMax)

    return RayResult.NoHit(this)
}

data class BoxIntersection(val tMin: Double, val normal: Vector3dc)

fun Box.intersect(origin: Vector3dc, direction: Vector3dc, invDirection: Vector3dc): BoxIntersection? {
    println("${min.x} ${min.y} ${min.z}")
    println("${max.x} ${max.y} ${max.z}")

    var tmin = (min.x - origin.x()) * invDirection.x()
    var tmax = (max.x - origin.x()) * invDirection.x()

    var enterAxis = 0

    if (tmin > tmax) {
        //swap(tmin, tmax)
        val temp = tmin
        tmin = tmax
        tmax = temp
    }

    var tymin = (min.y - origin.y()) * invDirection.y()
    var tymax = (max.y - origin.y()) * invDirection.y()

    if (tymin > tymax) {
        //swap(tymin, tymax)
        val temp = tymin
        tymin = tymax
        tymax = temp
    }

    if (tmin > tymax || tymin > tmax)
        return null

    if (tymin > tmin) {
        tmin = tymin
        enterAxis = 1
    }

    if (tymax < tmax) {
        tmax = tymax
    }

    var tzmin = (min.z - origin.z()) * invDirection.z()
    var tzmax = (max.z - origin.z()) * invDirection.z()

    if (tzmin > tzmax) {
        //swap(tzmin, tzmax)
        val temp = tzmin
        tzmin = tzmax
        tzmax = temp
    }

    if (tmin > tzmax || tzmin > tmax)
        return null

    if (tzmin > tmin) {
        tmin = tzmin
        enterAxis = 2
    }

    if (tzmax < tmax) {
        tmax = tzmax
    }

    fun one(a: Int) = if(a == enterAxis) 1.0 else 0.0
    val normal = Vector3d(one(0), one(1), one(2))
    if(normal.dot(direction) > 0)
        normal.negate()

    return BoxIntersection(tmin, normal)
}