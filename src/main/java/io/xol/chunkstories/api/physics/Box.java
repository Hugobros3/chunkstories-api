//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.physics;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** AABB box used for various collision testing purposes */
public final class Box {
	public double xPosition, yPosition, zPosition;
	public double xWidth, yHeight, zWidth;

	/** Creates a Box from another Box, copying it's properties */
	public Box(Box box) {
		xPosition = box.xPosition;
		yPosition = box.yPosition;
		zPosition = box.zPosition;

		xWidth = box.xWidth;
		yHeight = box.yHeight;
		zWidth = box.zWidth;
	}

	/** Creates a Box at position (0, 0, 0) and size as specified */
	public Box(double xwidth, double height, double zwidth) {
		xWidth = xwidth;
		yHeight = height;
		zWidth = zwidth;
	}

	/** Creates a Box, position and size as specified */
	public Box(double xPosition, double yPosition, double zPosition, double xWidth, double yHeight, double zWidth) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.xWidth = xWidth;
		this.yHeight = yHeight;
		this.zWidth = zWidth;
	}

	/** Creates a Box, position and size as specified */
	public Box(Vector3dc position, Vector3dc size) {
		this(position.x(), position.y(), position.z(), size.x(), size.y(), size.z());
	}

	@Nonnull
	public Box translate(double x, double y, double z) {
		xPosition += x;
		yPosition += y;
		zPosition += z;
		return this;
	}

	@Nonnull
	public Box translate(Vector3dc vec3) {
		xPosition += vec3.x();
		yPosition += vec3.y();
		zPosition += vec3.z();
		return this;
	}

	public boolean collidesWith(World world) {
		// TODO actually iterate over the space rather than assuming a small volume
		if (world.peekSafely((int) (xPosition + xWidth), (int) (yPosition + yHeight), (int) (zPosition + zWidth)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition + xWidth), (int) (yPosition), (int) (zPosition + zWidth)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition), (int) (yPosition + yHeight), (int) (zPosition + zWidth)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition), (int) (yPosition), (int) (zPosition + zWidth)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition + xWidth), (int) (yPosition + yHeight), (int) (zPosition)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition + xWidth), (int) (yPosition), (int) (zPosition)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition), (int) (yPosition + yHeight), (int) (zPosition)).getVoxel().isSolid())
			return true;
		if (world.peekSafely((int) (xPosition), (int) (yPosition), (int) (zPosition)).getVoxel().isSolid())
			return true;
		return false;
	}

	boolean inBox(Vector3dc hit, Vector3dc B1, Vector3dc B2, int axis) {
		if (axis == 1 && hit.z() > B1.z() && hit.z() < B2.z() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		if (axis == 2 && hit.z() > B1.z() && hit.z() < B2.z() && hit.x() > B1.x() && hit.x() < B2.x())
			return true;
		if (axis == 3 && hit.x() > B1.x() && hit.x() < B2.x() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		return false;
	}

	/** Box / Line collision check Returns null if no collision, a Vector3dm if
	 * collision, containing the collision point.
	 * 
	 * @return The collision point, or NULL. */
	@Nullable
	public Vector3dc lineIntersection(Vector3dc lineStart, Vector3dc lineDirectionIn) {
		double minDist = 0.0;
		double maxDist = 256d;

		Vector3d min = new Vector3d(xPosition, yPosition, zPosition);
		Vector3d max = new Vector3d(xPosition + xWidth, yPosition + yHeight, zPosition + zWidth);

		Vector3d lineDirection = new Vector3d(lineDirectionIn);
		lineDirection.normalize();

		Vector3d invDir = new Vector3d(1f / lineDirection.x(), 1f / lineDirection.y(), 1f / lineDirection.z());

		boolean signDirX = invDir.x() < 0;
		boolean signDirY = invDir.y() < 0;
		boolean signDirZ = invDir.z() < 0;

		Vector3d bbox = signDirX ? max : min;
		double tmin = (bbox.x() - lineStart.x()) * invDir.x();
		bbox = signDirX ? min : max;
		double tmax = (bbox.x() - lineStart.x()) * invDir.x();
		bbox = signDirY ? max : min;
		double tymin = (bbox.y() - lineStart.y()) * invDir.y();
		bbox = signDirY ? min : max;
		double tymax = (bbox.y() - lineStart.y()) * invDir.y();

		if ((tmin > tymax) || (tymin > tmax)) {
			return null;
		}
		if (tymin > tmin) {
			tmin = tymin;
		}
		if (tymax < tmax) {
			tmax = tymax;
		}

		bbox = signDirZ ? max : min;
		double tzmin = (bbox.z() - lineStart.z()) * invDir.z();
		bbox = signDirZ ? min : max;
		double tzmax = (bbox.z() - lineStart.z()) * invDir.z();

		if ((tmin > tzmax) || (tzmin > tmax)) {
			return null;
		}
		if (tzmin > tmin) {
			tmin = tzmin;
		}
		if (tzmax < tmax) {
			tmax = tzmax;
		}
		if ((tmin < maxDist) && (tmax > minDist)) {

			Vector3d intersect = new Vector3d(lineStart);

			intersect.add(lineDirection.mul(tmin));
			return intersect;
			// return Vector3dm.add(lineStart,
			// lineDirection.clone().normalize().scale(tmin), null);

			// return ray.getPointAtDistance(tmin);
		}
		return null;

	}

	@Override
	public String toString() {
		return "Collision Box : position = [" + xPosition + ", " + yPosition + ", " + zPosition + "] size = [" + xWidth + ", " + yHeight + ", " + zWidth + "]";
	}

	public boolean collidesWith(Box b) {
		if (yPosition + yHeight <= b.yPosition || yPosition >= b.yPosition + b.yHeight || xPosition + xWidth <= b.xPosition
				|| xPosition >= b.xPosition + b.xWidth || zPosition + zWidth <= b.zPosition || zPosition >= b.zPosition + b.zWidth) {
			return false;
		}
		return true;
	}

	public boolean isPointInside(double posX, double posY, double posZ) {
		if (yPosition + yHeight < posY || yPosition > posY || xPosition + xWidth < posX || xPosition > posX || zPosition + zWidth < posZ || zPosition > posZ) {
			return false;
		}

		return true;
	}

	public boolean isPointInside(Vector3dc vec) {
		return isPointInside(vec.x(), vec.y(), vec.z());
	}
}
