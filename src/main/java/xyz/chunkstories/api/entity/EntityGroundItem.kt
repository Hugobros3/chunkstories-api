//
// This file is a part of the Chunk Stories Core codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityDefinition
import xyz.chunkstories.api.entity.traits.ItemOnGroundContents
import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.entity.traits.serializable.TraitVelocity
import xyz.chunkstories.api.item.inventory.InventoryOwner
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.WorldClient
import xyz.chunkstories.api.world.WorldMaster

class EntityGroundItem(definition: EntityDefinition, world: World) : Entity(definition, world), InventoryOwner {
    protected var rotation = 0f

    val entityVelocity = TraitVelocity(this)
    val collisions = TraitCollidable(this)

    var spawnTime: Long = 0

    val inventory = TraitInventory(this, 1, 1)

    init {
        //traitContents = ItemOnGroundContents(this)
        EntityGroundItemRenderer(this)
        spawnTime = System.currentTimeMillis()
    }

    fun canBePickedUpYet(): Boolean {
        return System.currentTimeMillis() - spawnTime > 2000L
    }

    override fun tick() {
        // this.moveWithCollisionRestrain(0, -0.05, 0);
        val velocity = entityVelocity.velocity

        if (world is WorldMaster) {
            val voxelIn = world.peekSafely(location).voxel
            val inWater = voxelIn?.liquid == true

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

            if (Math.abs(velocity.x) < 0.02)
                velocity.x = 0.0
            if (Math.abs(velocity.z) < 0.02)
                velocity.z = 0.0

            if (Math.abs(velocity.y) < 0.01)
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

    /*static class EntityGroundItemRenderer extends EntityRenderer<EntityGroundItem> {

		@Override
		public int renderEntities(RenderingInterface renderer,
				RenderingIterator<EntityGroundItem> renderableEntitiesIterator) {
			renderer.useShader("entities");

			int i = 0;

			while (renderableEntitiesIterator.hasNext()) {
				EntityGroundItem e = renderableEntitiesIterator.next();

				ItemPile within = e.itemPileWithin.getItemPile();
				if (within != null) {
					CellData cell = e.getWorld().peekSafely(e.getLocation());
					renderer.currentShader().setUniform2f("worldLightIn", cell.getBlocklight(), cell.getSunlight());

					Matrix4f matrix = new Matrix4f();

					Vector3d loc = e.getLocation().add(0.0, 0.25, 0.0);
					matrix.translate((float) loc.x,
							(float) (loc.y + Math.sin(Math.PI / 180 * e.rotation * 2) * 0.125 + 0.25), (float) loc.z);
					// matrix.rotate((float)Math.PI/2, new Vector3f(1,0 ,0));
					matrix.rotate((float) Math.PI / 180 * e.rotation, new Vector3f(0, 1, 0));
					within.getItem().getDefinition().getRenderer().renderItemInWorld(renderer, within, e.getWorld(),
							e.getLocation(), matrix);
					// renderingInterface.flush();
				} else {
					System.out.println("EntityGroundItem: Not within any inventory ???");
				}

				i++;
			}

			return i;
		}

		@Override
		public void freeRessources() {
			// Not much either
		}

	}*/

    override fun getBoundingBox(): Box {
        return Box(0.5, 0.75, 0.5).translate(-0.25, 0.0, -0.25)
    }
}
