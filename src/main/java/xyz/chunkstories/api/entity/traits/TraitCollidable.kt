//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.content.json.asBoolean
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.physics.overlaps

open class TraitCollidable(entity: Entity) : Trait(entity) {
    override val traitName = "collisions"

    var selectable: Boolean
    var collidesWithEntities: Boolean

    init {
        val collisions = entity.definition.properties["collisions"]?.asDict
        collidesWithEntities = collisions?.get("withOtherEntities")?.asBoolean ?: false
        selectable = collisions?.get("selectable")?.asBoolean ?: true
    }

    val isOnGround: Boolean
        get() = if (isStuckInEntity == null)
            entity.world.collisionsManager.runEntityAgainstWorldVoxelsAndEntities(entity, entity.location, onGroundTest_)
                    .length() != 0.0
        else
            canMoveWithCollisionRestrain(onGroundTest_).length() != 0.0

    // Fine
    val isStuckInEntity: Entity?
        get() {
            val bbox = entity.getTranslatedBoundingBox()
            val candidates = entity.world.getEntitiesInBox(bbox)
            for (adverseEntity in candidates) {
                if (adverseEntity != entity) {
                    val adverseEntityTraitCollidable = adverseEntity.traits[TraitCollidable::class.java] ?: continue
                    if (adverseEntityTraitCollidable.collidesWithEntities) {
                        if (adverseEntity.getTranslatedBoundingBox().collidesWith(this.entity.getTranslatedBoundingBox()))
                            for (b in adverseEntityTraitCollidable.translatedCollisionBoxes)
                                for (c in this.translatedCollisionBoxes)
                                    if (overlaps(b, c))
                                        return adverseEntity
                    }

                }
            }
            return null
        }

    open val collisionBoxes: Array<Box>
        get() = arrayOf(entity.getBoundingBox())

    val translatedCollisionBoxes: Array<Box>
        get() {
            val boxes = collisionBoxes
            for (box in boxes)
                box.translate(entity.location)
            return boxes
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
            //println("$entity stuck in $stuckIn")
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
