//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.physics.Box;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;

import javax.annotation.Nullable;

public class TraitCollidable extends Trait {

	private boolean collidesWithEntities = true;
	public TraitCollidable(Entity entity) {
		super(entity);
	}

	public Vector3dc moveWithCollisionRestrain(Vector3dc delta) {
		Vector3dc movementLeft = entity.getWorld().getCollisionsManager().runEntityAgainstWorldVoxels(entity, entity.getLocation(), delta);

		entity.traitLocation.move(delta.x() - movementLeft.x(), delta.y() - movementLeft.y(), delta.z() - movementLeft.z());
		return movementLeft;
	}

	public Vector3dc moveWithCollisionRestrain(double mx, double my, double mz) {
		return moveWithCollisionRestrain(new Vector3d(mx, my, mz));
	}

	/** Does the hitboxes computations to determine if that entity could move that
	 * delta
	 * 
	 * @return The remaining distance in each dimension if he got stuck ( with
	 *         vec3(0.0, 0.0, 0.0) meaning it can move without colliding with
	 *         anything ) */
	public Vector3dc canMoveWithCollisionRestrain(Vector3dc delta) {
		return entity.getWorld().getCollisionsManager().runEntityAgainstWorldVoxels(entity, entity.getLocation(), delta);
	}

	public Vector3dc canMoveWithCollisionRestrain(double dx, double dy, double dz) {
		return canMoveWithCollisionRestrain(new Vector3d(dx, dy, dz));
	}

	/** Does the hitboxes computations to determine if that entity could move that
	 * delta
	 * 
	 * @param from Change the origin of the movement from the default ( current
	 *            entity position )
	 * @return The remaining distance in each dimension if he got stuck ( with
	 *         vec3(0.0, 0.0, 0.0) meaning it can move without colliding with
	 *         anything ) */
	public Vector3dc canMoveWithCollisionRestrain(Vector3dc from, Vector3dc delta) {
		return entity.getWorld().getCollisionsManager().runEntityAgainstWorldVoxels(entity, from, delta);
	}

	private static final Vector3dc onGroundTest_ = new Vector3d(0.0, -0.01, 0.0);

	public boolean isOnGround() {
		// System.out.println(canMoveWithCollisionRestrain(onGroundTest_).length());
		if (isStuckInEntity() == null)
			return entity.getWorld().getCollisionsManager().runEntityAgainstWorldVoxelsAndEntities(entity, entity.getLocation(), onGroundTest_).length() != 0.0d;
		else
			return canMoveWithCollisionRestrain(onGroundTest_).length() != 0.0d;
	}

	public void unstuck() {
		Entity stuckIn = this.isStuckInEntity();
		if (stuckIn != null) {
			Vector3d delta = entity.getLocation();
			delta.sub(stuckIn.getLocation());
			delta.add(0.01, 0.01, 0.01);

			this.moveWithCollisionRestrain(delta);
		}
	}

	@Nullable
	public Entity isStuckInEntity() {
		for (Entity e : entity.getWorld().getEntitiesInBox(entity.getLocation(), new Vector3d(1, 2, 1))) {
			if (e != entity) {
				TraitCollidable tc = e.traits.get(TraitCollidable.class);
				if (tc != null) {
					if (collidesWithEntities) {

						if (tc.getBoundingBox().collidesWith(this.getBoundingBox()))
							// Fine
							for (Box b : tc.getTranslatedCollisionBoxes())
								for (Box c : this.getTranslatedCollisionBoxes())
									if (b.collidesWith(c))
										return e;
					}
				}
			}
		}
		return null;
	}

	public Box getTranslatedBoundingBox() {
		Box box = getBoundingBox();
		box.translate(entity.getLocation());
		return box;
	}

	public Box getBoundingBox() {
		return new Box(1.0, 1.0, 1.0).translate(-0.5, 0, -0.5);
	}

	public Box[] getCollisionBoxes() {
		return new Box[] { getBoundingBox() };
	}

	public Box[] getTranslatedCollisionBoxes() {
		Box[] boxes = getCollisionBoxes();
		for (Box box : boxes)
			box.translate(entity.getLocation());
		return boxes;
	}
}
