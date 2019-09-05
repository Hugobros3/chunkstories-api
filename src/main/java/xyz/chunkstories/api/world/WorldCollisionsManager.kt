//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.joml.Vector3dc

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity

interface WorldCollisionsManager {
    /** Does a complicated check to see how far the entity can go using the delta
     * direction, from the 'start' position. Does not actually move anything Returns
     * the remaining distance in each dimension if it got stuck ( with vec3(0.0,
     * 0.0, 0.0) meaning it can safely move without colliding with anything )  */
    fun runEntityAgainstWorldVoxels(entity: Entity, from: Vector3dc, delta: Vector3dc): Vector3dc

    fun runEntityAgainstWorldVoxelsAndEntities(entity: Entity, from: Vector3dc, delta: Vector3dc): Vector3dc

    /** Tries to move the entity and returns the remaining untravelled distance,
     * will collide with other entities!  */
    fun tryMovingEntityWithCollisions(entity: Entity, from: Vector3dc, delta: Vector3dc): Vector3dc

    fun isPointSolid(point: Vector3dc): Boolean
}