//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.physics.Box
import org.joml.Vector3d
import org.joml.Vector3dc

import xyz.chunkstories.api.entity.Entity

open class TraitCollidable(entity: Entity) : Trait(entity) {

    var collidesWithEntities = true

    // System.out.println(canMoveWithCollisionRestrain(onGroundTest_).length());
    val isOnGround: Boolean
        get() = if (isStuckInEntity == null)
            entity.world.collisionsManager.runEntityAgainstWorldVoxelsAndEntities(entity, entity.location, onGroundTest_)
                    .length() != 0.0
        else
            canMoveWithCollisionRestrain(onGroundTest_).length() != 0.0

    // Fine
    val isStuckInEntity: Entity?
        get() {
            for (e in entity.world.getEntitiesInBox(entity.location, Vector3d(1.0, 2.0, 1.0))) {
                if (e !== entity) {
                    val tc = e.traits[TraitCollidable::class.java]
                    if (tc != null) {
                        if (collidesWithEntities) {

                            if (tc.boundingBox.collidesWith(this.boundingBox))
                                for (b in tc.translatedCollisionBoxes)
                                    for (c in this.translatedCollisionBoxes)
                                        if (b.collidesWith(c))
                                            return e
                        }
                    }
                }
            }
            return null
        }

    val translatedBoundingBox: Box
        get() {
            val box = boundingBox
            box.translate(entity.location)
            return box
        }

    open val boundingBox: Box
        get() = Box(1.0, 1.0, 1.0).translate(-0.5, 0.0, -0.5)

    open val collisionBoxes: Array<Box>
        get() = arrayOf(boundingBox)

    val translatedCollisionBoxes: Array<Box>
        get() {
            val boxes = collisionBoxes
            for (box in boxes)
                box.translate(entity.location)
            return boxes
        }

    init {
        collidesWithEntities = entity.definition.resolveProperty("collisions.withOtherEntities", "true") == "true"
    }

    fun moveWithCollisionRestrain(delta: Vector3dc): Vector3dc {
        val movementLeft = entity.world.collisionsManager.runEntityAgainstWorldVoxels(entity, entity.location, delta)

        entity.traitLocation.move(delta.x() - movementLeft.x(), delta.y() - movementLeft.y(), delta.z() - movementLeft.z())
        return movementLeft
    }

    fun moveWithCollisionRestrain(mx: Double, my: Double, mz: Double): Vector3dc {
        return moveWithCollisionRestrain(Vector3d(mx, my, mz))
    }

    /** Does the hitboxes computations to determine if that entity could move that
     * delta
     *
     * @return The remaining distance in each dimension if he got stuck ( with
     * vec3(0.0, 0.0, 0.0) meaning it can move without colliding with
     * anything )
     */
    fun canMoveWithCollisionRestrain(delta: Vector3dc): Vector3dc {
        return entity.world.collisionsManager.runEntityAgainstWorldVoxels(entity, entity.location, delta)
    }

    fun canMoveWithCollisionRestrain(dx: Double, dy: Double, dz: Double): Vector3dc {
        return canMoveWithCollisionRestrain(Vector3d(dx, dy, dz))
    }

    /** Does the hitboxes computations to determine if that entity could move that
     * delta
     *
     * @param from Change the origin of the movement from the default ( current
     * entity position )
     * @return The remaining distance in each dimension if he got stuck ( with
     * vec3(0.0, 0.0, 0.0) meaning it can move without colliding with
     * anything )
     */
    fun canMoveWithCollisionRestrain(from: Vector3dc, delta: Vector3dc): Vector3dc {
        return entity.world.collisionsManager.runEntityAgainstWorldVoxels(entity, from, delta)
    }

    fun unstuck() {
        val stuckIn = this.isStuckInEntity
        if (stuckIn != null) {
            val delta = entity.location
            delta.sub(stuckIn.location)
            delta.add(0.01, 0.01, 0.01)

            this.moveWithCollisionRestrain(delta)
        }
    }

    companion object {
        private val onGroundTest_ = Vector3d(0.0, -0.01, 0.0)
    }
}
