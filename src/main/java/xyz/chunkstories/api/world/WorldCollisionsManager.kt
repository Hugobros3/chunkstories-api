//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import org.joml.Vector3dc

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.physics.EntityHitbox
import xyz.chunkstories.api.world.cell.Cell

interface WorldCollisionsManager {
    /** Raytraces throught the world to find a solid block
     *
     * @param limit Between 0 and a finite number
     * @return The exact location of the intersection or null if it didn't found
     * one
     */
    fun raytraceSolid(initialPosition: Vector3dc, direction: Vector3dc, limit: Double): Location?

    /** Raytraces throught the world to find a solid block
     *
     * @param limit Between 0 and a finite number
     * @return The exact location of the step just before the intersection ( as to
     * get the adjacent block ) or null if it didn't found one
     */
    fun raytraceSolidOuter(initialPosition: Vector3dc, direction: Vector3dc, limit: Double): Location?

    /** Raytraces throught the world to find a solid or selectable block
     *
     * @param limit Between 0 and a finite number
     * @return The exact location of the intersection or null if it didn't found
     * one
     */
    fun raytraceSelectable(initialPosition: Location, direction: Vector3dc, limit: Double): Location?

    /** Takes into account the voxel terrain and will stop at a solid block,
     * **warning** limit can't be == -1 !
     *
     * @param limit Between 0 and a finite number
     * @return Returns all entities that intersects with the ray within the limit,
     * ordered nearest to furthest
     */
    fun rayTraceEntities(initialPosition: Vector3dc, direction: Vector3dc, limit: Double): Iterator<Entity>

    /** Ignores any terrain
     *
     * @param limit Either -1 or between 0 and a finite number
     * @return Returns all entities that intersects with the ray within the limit,
     * ordered nearest to furthest
     */
    fun raytraceEntitiesIgnoringVoxels(initialPosition: Vector3dc, direction: Vector3dc, limit: Double): Iterator<Entity>

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