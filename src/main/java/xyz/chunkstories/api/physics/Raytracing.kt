package xyz.chunkstories.api.physics

import org.joml.*
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.TraitHitboxes
import xyz.chunkstories.api.world.cell.Cell
import kotlin.math.sqrt

data class RayQuery(val origin: Location,
                    val direction: Vector3dc,
                    val tMin: Double = 0.0,
                    val tMax: Double = 256.0,
                    val voxelMask: (Cell) -> Boolean = { it.voxel.solid },
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

        class VoxelHit(val cell: Cell, hitPosition: Vector3dc, t: Double, normal: Vector3dc, rayQuery: RayQuery) : Hit(hitPosition, t, normal, rayQuery) {
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
    var cell: Cell

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
    var bestEntityHit: RayResult.Hit.EntityHit? = null

    var tMinVoxel = Double.MAX_VALUE
    var bestVoxelHit: RayResult.Hit.VoxelHit? = null

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
            val potentialEntities = this.origin.world.getEntitiesInBox(Box.fromExtents(32.0, 32.0, 32.0).translate(x - 16.0, y - 16.0, z - 16.0))
            for (entity in potentialEntities) {
                // Consider each entity only once
                if (consideredEntities.add(entity) && this.entityMask(entity)) {
                    val traitCollisions = entity.traits[TraitCollidable::class] ?: continue
                    val traitHitboxes = entity.traits[TraitHitboxes::class]

                    if (traitHitboxes == null) {
                        for (box in traitCollisions.translatedCollisionBoxes) {
                            val intersection = box.intersect(origin, direction, this.invDir)
                            if (intersection != null) {
                                //val distance = collisionPoint.distance(origin)
                                if (intersection.tMin > this.tMin && intersection.tMin <= this.tMax) {
                                    if (intersection.tMin < tMinEntity) {
                                        tMinEntity = intersection.tMin
                                        bestEntityHit = RayResult.Hit.EntityHit(entity, null, intersection.hitPosition, intersection.tMin, intersection.normal, this)
                                    }
                                }
                            }
                        }
                    } else {
                        for (hitBox in traitHitboxes.hitBoxes) {
                            val intersection = hitBox.intersect(origin, direction, this.invDir)
                            if (intersection != null) {
                                //val distance = collisionPoint.distance(origin)
                                if (intersection.tMin > this.tMin && intersection.tMin <= this.tMax) {
                                    if (intersection.tMin < tMinEntity) {
                                        tMinEntity = intersection.tMin
                                        bestEntityHit = RayResult.Hit.EntityHit(entity, hitBox, intersection.hitPosition, intersection.tMin, intersection.normal, this)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        cell = this.origin.world.peek(x, y, z)
        val voxel = cell.voxel

        if (this.voxelMask(cell)) {
            if (voxel.isAir())
                continue

            for (box in cell.translatedCollisionBoxes ?: emptyArray()) {
                val intersection = box.intersect(origin, direction, this.invDir)
                if (intersection != null) {
                    //val distance = collisionPoint.distance(origin)
                    val distance = intersection.tMin
                    if (distance > this.tMin && distance <= this.tMax) {
                        if (distance < tMinVoxel) {
                            tMinVoxel = distance
                            bestVoxelHit = RayResult.Hit.VoxelHit(cell, intersection.hitPosition, intersection.tMin, intersection.normal, this)
                        }
                    }
                }
            }
            if (tMinVoxel != Double.MAX_VALUE) {
                if (tMinVoxel < tMinEntity) {
                    return bestVoxelHit!!
                } else if (bestEntityHit != null) {
                    return bestEntityHit
                }
            }
        }

        // distance += deltaDist[side];

    } while (voxelDelta[0] * voxelDelta[0] + voxelDelta[1] * voxelDelta[1] + voxelDelta[2] * voxelDelta[2] < this.tMax * this.tMax)

    return RayResult.NoHit(this)
}

data class BoxIntersection(val tMin: Double, val hitPosition: Vector3dc, val normal: Vector3dc)

fun Box.intersect(origin: Vector3dc, direction: Vector3dc, invDirection: Vector3dc): BoxIntersection? {
    //println("${min.x} ${min.y} ${min.z}")
    //println("${max.x} ${max.y} ${max.z}")

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

    fun one(a: Int) = if (a == enterAxis) 1.0 else 0.0
    val normal = Vector3d(one(0), one(1), one(2))
    if (normal.dot(direction) > 0)
        normal.negate()

    val hitPosition = Vector3d(origin)
    hitPosition.add(direction.x() * tmin, direction.y() * tmin, direction.z() * tmin)

    return BoxIntersection(tmin, hitPosition, normal)
}

fun EntityHitbox.intersect(lineStart: Vector3dc, lineDirection: Vector3dc, invDirection: Vector3dc): BoxIntersection? {
    val fromAABBToWorld = Matrix4f()
    if (this.animationTrait != null)
        fromAABBToWorld.set(animationTrait.animatedSkeleton.getBoneHierarchyTransformationMatrix(name, (System.currentTimeMillis() % 1000000).toDouble()))

    val worldPositionTransformation = Matrix4f()

    val entityLoc = entity.location

    val pos = Vector3f(entityLoc.x.toFloat(), entityLoc.y.toFloat(), entityLoc.z.toFloat())
    worldPositionTransformation.translate(pos)

    // Creates from AABB space to worldspace
    worldPositionTransformation.mul(fromAABBToWorld, fromAABBToWorld)

    // Invert it.
    val fromWorldToAABB = Matrix4f()
    fromAABBToWorld.invert(fromWorldToAABB)

    // Transform line start into AABB space
    val lineStart4 = Vector4f(lineStart.x().toFloat(), lineStart.y().toFloat(), lineStart.z().toFloat(), 1.0f)
    val lineDirection4 = Vector4f(lineDirection.x().toFloat(), lineDirection.y().toFloat(), lineDirection.z().toFloat(), 0.0f)

    fromWorldToAABB.transform(lineStart4)
    fromWorldToAABB.transform(lineDirection4)

    val lineStartTransformed = Vector3d(lineStart4.x().toDouble(), lineStart4.y().toDouble(), lineStart4.z().toDouble())
    val lineDirectionTransformed = Vector3d(lineDirection4.x().toDouble(), lineDirection4.y().toDouble(), lineDirection4.z().toDouble())

    // Actual computation
    val intersection = box.intersect(lineStartTransformed, lineDirectionTransformed, invDirection) ?: return null

    // Transform hitPoint back into world
    val hitPoint = intersection.hitPosition
    val hitPoint4 = Vector4f(hitPoint.x().toFloat(), hitPoint.y().toFloat(), hitPoint.z().toFloat(), 1.0f)
    fromAABBToWorld.transform(hitPoint4)
    hitPoint4.mul(1.0f / hitPoint4.w())

    val normal = intersection.normal
    val normal4 = Vector4f(normal.x().toFloat(), normal.y().toFloat(), normal.z().toFloat(), 0.0f)
    fromAABBToWorld.transform(normal4)
    //normal4.mul(1.0f / normal4.w())

    val hitPointWS = Vector3d(hitPoint4.x().toDouble(), hitPoint4.y().toDouble(), hitPoint4.z().toDouble())
    val normalWS = Vector3d(normal4.x().toDouble(), normal4.y().toDouble(), normal4.z().toDouble())
    return BoxIntersection(intersection.tMin, hitPointWS, normalWS)
}