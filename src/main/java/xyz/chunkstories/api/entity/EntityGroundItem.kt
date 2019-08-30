//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.entity.traits.serializable.TraitVelocity
import xyz.chunkstories.api.item.inventory.InventoryOwner
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldClient
import xyz.chunkstories.api.world.WorldMaster
import kotlin.math.abs

class EntityGroundItem(definition: EntityDefinition, world: World) : Entity(definition, world), InventoryOwner {
    protected var rotation = 0f

    val entityVelocity = TraitVelocity(this)
    val collisions = TraitCollidable(this)

    var spawnTime: Long = 0

    val inventory = TraitInventory(this, 1, 1, true)

    init {
        EntityGroundItemRenderer(this)
        spawnTime = System.currentTimeMillis()
    }

    fun canBePickedUpYet(): Boolean {
        return System.currentTimeMillis() - spawnTime > 2000L
    }

    override fun tick() {
        val velocity = entityVelocity.velocity

        if (world is WorldMaster) {
            val inWater = world.peek(location).voxel.liquid

            val terminalVelocity = if (inWater) -0.25 else -0.5
            if (velocity.y() > terminalVelocity && !collisions.isOnGround)
                velocity.y = velocity.y() - 0.016
            if (velocity.y() < terminalVelocity)
                velocity.y = terminalVelocity

            val remainingToMove = collisions.moveWithCollisionRestrain(velocity.x(), velocity.y(), velocity.z())
            if (remainingToMove.y() < -0.02 && collisions.isOnGround) {
                if (remainingToMove.y() < -0.01) {
                    // Bounce
                    val originalDownardsVelocity = velocity.y()
                    val bounceFactor = 0.15
                    velocity.mul(bounceFactor)
                    velocity.y = -originalDownardsVelocity * bounceFactor

                    // world.getSoundManager().playSoundEffect("./sounds/dogez/weapon/grenades/grenade_bounce.ogg",
                    // Mode.NORMAL, getLocation(), 1, 1, 10, 35);
                } else
                    velocity.mul(0.0)
            }

            velocity.x *= 0.98
            velocity.z *= 0.98

            if (abs(velocity.x) < 0.02)
                velocity.x = 0.0
            if (abs(velocity.z) < 0.02)
                velocity.z = 0.0

            if (abs(velocity.y) < 0.01)
                velocity.y = 0.0

            entityVelocity.setVelocity(velocity)
        }

        if (world is WorldClient) {

            if (collisions.isOnGround) {
                rotation += 1.0f
                rotation %= 360f
            }
        }
    }

    override fun getBoundingBox(): Box {
        return Box.fromExtentsCenteredHorizontal(0.5, 0.75, 0.5)
    }
}
