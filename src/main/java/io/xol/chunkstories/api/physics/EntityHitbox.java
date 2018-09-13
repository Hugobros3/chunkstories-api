//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.physics;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector4f;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.TraitAnimated;

public class EntityHitbox {
	final Entity entity;
	final TraitAnimated animationTrait;

	final Box box;
	final String skeletonPart;

	public EntityHitbox(Entity entity, Box box, String skeletonPart) {
		this.entity = entity;
		this.animationTrait = entity.traits.get(TraitAnimated.class);

		this.box = box;
		this.skeletonPart = skeletonPart;
	}

	/** Tricky maths; transforms the inbound ray so the hitbox would be at 0.0.0 and
	 * axis-aligned */
	public Vector3dc lineIntersection(Vector3dc lineStart, Vector3dc lineDirection) {
		Matrix4f fromAABBToWorld = new Matrix4f();
		if (this.animationTrait != null)
			fromAABBToWorld.set(animationTrait.getAnimatedSkeleton().getBoneHierarchyTransformationMatrix(skeletonPart, System.currentTimeMillis() % 1000000));

		Matrix4f worldPositionTransformation = new Matrix4f();

		Location entityLoc = entity.getLocation();

		Vector3f pos = new Vector3f((float) entityLoc.x, (float) entityLoc.y, (float) entityLoc.z);
		worldPositionTransformation.translate(pos);

		// Creates from AABB space to worldspace
		worldPositionTransformation.mul(fromAABBToWorld, fromAABBToWorld);

		// Invert it.
		Matrix4f fromWorldToAABB = new Matrix4f();
		fromAABBToWorld.invert(fromWorldToAABB);

		// Transform line start into AABB space
		Vector4f lineStart4 = new Vector4f((float) lineStart.x(), (float) lineStart.y(), (float) lineStart.z(), 1.0f);
		Vector4f lineDirection4 = new Vector4f((float) lineDirection.x(), (float) lineDirection.y(), (float) lineDirection.z(), 0.0f);

		fromWorldToAABB.transform(lineStart4);
		fromWorldToAABB.transform(lineDirection4);

		Vector3d lineStartTransformed = new Vector3d(lineStart4.x(), lineStart4.y(), lineStart4.z());
		Vector3d lineDirectionTransformed = new Vector3d(lineDirection4.x(), lineDirection4.y(), lineDirection4.z());

		// Actual computation
		Vector3dc hitPoint = box.lineIntersection(lineStartTransformed, lineDirectionTransformed);
		if (hitPoint == null)
			return null;

		// Transform hitPoint back into world
		Vector4f hitPoint4 = new Vector4f((float) hitPoint.x(), (float) hitPoint.y(), (float) hitPoint.z(), 1.0f);
		fromAABBToWorld.transform(hitPoint4);

		return new Vector3d((double) (float) hitPoint4.x(), (double) (float) hitPoint4.y(), (double) (float) hitPoint4.z());
	}

	public String getName() {
		return skeletonPart;
	}
}