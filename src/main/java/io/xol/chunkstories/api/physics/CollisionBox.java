package io.xol.chunkstories.api.physics;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.Content.Voxels;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
// http://chunkstories.xyz
// http://xol.io

public final class CollisionBox implements Collidable
{
	public double xpos, ypos, zpos;
	public double xw, h, zw;

	public CollisionBox(CollisionBox box)
	{
		xw = box.xw;
		h = box.h;
		zw = box.zw;
		xpos = box.xpos;
		ypos = box.ypos;
		zpos = box.zpos;
	}

	public CollisionBox(double xwidth, double height, double zwidth)
	{
		xw = xwidth;
		h = height;
		zw = zwidth;
	}

	public CollisionBox(double xpos, double ypos, double zpos, double xw, double h, double zw)
	{
		this.xpos = xpos;
		this.ypos = ypos;
		this.zpos = zpos;
		this.xw = xw;
		this.h = h;
		this.zw = zw;
	}

	public CollisionBox translate(double x, double y, double z)
	{
		xpos += x;
		ypos += y;
		zpos += z;
		return this;
	}

	public CollisionBox translate(Vector3dc vec3)
	{
		xpos += vec3.x();
		ypos += vec3.y();
		zpos += vec3.z();
		return this;
	}

	public boolean collidesWith(World world)
	{
		Voxels store = world.getGameContext().getContent().voxels();
		if (store.getVoxelById(world.getVoxelData((int) (xpos + xw), (int) (ypos + h), (int) (zpos + zw))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos + xw), (int) (ypos), (int) (zpos + zw))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos), (int) (ypos + h), (int) (zpos + zw))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos), (int) (ypos), (int) (zpos + zw))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos + xw), (int) (ypos + h), (int) (zpos))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos + xw), (int) (ypos), (int) (zpos))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos), (int) (ypos + h), (int) (zpos))).getType().isSolid())
			return true;
		if (store.getVoxelById(world.getVoxelData((int) (xpos), (int) (ypos), (int) (zpos))).getType().isSolid())
			return true;
		return false;
	}

	boolean inBox(Vector3dc hit, Vector3dc B1, Vector3dc B2, int axis)
	{
		if (axis == 1 && hit.z() > B1.z() && hit.z() < B2.z() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		if (axis == 2 && hit.z() > B1.z() && hit.z() < B2.z() && hit.x() > B1.x() && hit.x() < B2.x())
			return true;
		if (axis == 3 && hit.x() > B1.x() && hit.x() < B2.x() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		return false;
	}

	/**
	 * Box / Line collision check Returns null if no collision, a Vector3dm if collision, containing the collision point.
	 * 
	 * @return The collision point, or NULL.
	 */
	public Vector3dc lineIntersection(Vector3dc lineStart, Vector3dc lineDirectionIn)
	{
		double minDist = 0.0;
		double maxDist = 256d;
		
		Vector3d min = new Vector3d(xpos, ypos, zpos);
		Vector3d max = new Vector3d(xpos + xw, ypos + h, zpos + zw);
		
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
			//return Vector3dm.add(lineStart, lineDirection.clone().normalize().scale(tmin), null);
			
			//return ray.getPointAtDistance(tmin);
		}
		return null;
		
	}
	
	public double getWidthWarp()
	{
		return xw % 1;
	}

	public double getLengthWarp()
	{
		return zw % 1;
	}

	public double getHeightWarp()
	{
		return h % 1;
	}

	@Override
	public String toString()
	{
		return "Collision Box : position = [" + xpos + ", " + ypos + ", " + zpos + "] size = [" + xw + ", " + h + ", " + zw + "]";
	}

	@Override
	public boolean collidesWith(Collidable c)
	{
		if(c instanceof CollisionBox)
			return collidesWith(c);
		
		throw new UnsupportedOperationException("Unupported Collidable: "+c.getClass().getSimpleName());
	}
	
	public boolean collidesWith(CollisionBox b)
	{
		if (ypos + h <= b.ypos || ypos >= b.ypos + b.h || xpos + xw<= b.xpos || xpos >= b.xpos + b.xw || zpos + zw <= b.zpos || zpos >= b.zpos + b.zw)
		{
			return false;
		}
		return true;
	}

	public boolean isPointInside(double posX, double posY, double posZ)
	{
		if (ypos + h < posY || ypos > posY || xpos + xw < posX || xpos > posX || zpos + zw < posZ || zpos > posZ)
		{
			return false;
		}

		return false;
	}

	/*public boolean collidesWith(Entity entity)
	{
		CollisionBox[] entityBoxes = entity.getTranslatedCollisionBoxes();
		if (entityBoxes != null)
			for (CollisionBox entityBox : entityBoxes)
				if (entityBox.collidesWith(entityBox))
					return true;
		return false;
	}*/
}
