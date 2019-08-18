//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.math.Math2.floor

import xyz.chunkstories.api.world.World
import java.lang.Math.ceil

/** AABB box used for various collision testing purposes  */
data class Box protected constructor(val min: Vector3d, val max: Vector3d) {
    val center: Vector3dc
        get() = Vector3d(min).add(max).mul(0.5)

    val extents: Vector3dc
        get() = Vector3d(max).sub(min)

    /** Creates a Box from another Box, copying it's properties  */
    constructor(box: Box) : this(Vector3d(box.min), Vector3d(box.max))

    /** Creates a Box at position (0, 0, 0) and size as specified  */
    /*constructor(xwidth: Double, height: Double, zwidth: Double) {
        xWidth = xwidth
        yHeight = height
        zWidth = zwidth
    }

    /** Creates a Box, position and size as specified  */
    constructor(xPosition: Double, yPosition: Double, zPosition: Double, xWidth: Double, yHeight: Double, zWidth: Double) {
        this.xPosition = xPosition
        this.yPosition = yPosition
        this.zPosition = zPosition
        this.xWidth = xWidth
        this.yHeight = yHeight
        this.zWidth = zWidth
    }*/

    fun translate(x: Double, y: Double, z: Double): Box {
        min.add(x, y, z)
        max.add(x, y, z)
        return this
    }

    fun translate(delta: Vector3dc): Box {
        min.add(delta)
        max.add(delta)
        return this
    }

    fun collidesWith(world: World): Boolean {
        val minx = floor(min.x).toInt()
        val miny = floor(min.y).toInt()
        val minz = floor(min.z).toInt()

        val maxx = ceil(max.x).toInt()
        val maxy = ceil(max.y).toInt()
        val maxz = ceil(max.z).toInt()

        for(x in minx..maxx) {
            for(y in miny..maxy) {
                for(z in minz..maxz) {
                    val cell = world.peekSafely(x, y, z)
                    if(cell.voxel.solid) {
                        val cbs = cell.translatedCollisionBoxes ?: continue
                        for (cb in cbs) {
                            if (cb.collidesWith(this))
                                return true
                        }
                    }
                }
            }
        }
        return false
    }

    /*boolean inBox(Vector3dc hit, Vector3dc B1, Vector3dc B2, int axis) {
		if (axis == 1 && hit.z() > B1.z() && hit.z() < B2.z() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		if (axis == 2 && hit.z() > B1.z() && hit.z() < B2.z() && hit.x() > B1.x() && hit.x() < B2.x())
			return true;
		if (axis == 3 && hit.x() > B1.x() && hit.x() < B2.x() && hit.y() > B1.y() && hit.y() < B2.y())
			return true;
		return false;
	}*/

    /** Box / Line collision check Returns null if no collision, a Vector3dm if
     * collision, containing the collision point.
     *
     * @return The collision point, or NULL.
     */
    fun lineIntersection(lineStart: Vector3dc, lineDirectionIn: Vector3dc): Vector3dc? {
        val minDist = 0.0
        val maxDist = 256.0

        val min = min
        val max = max

        val lineDirection = Vector3d(lineDirectionIn)
        lineDirection.normalize()

        val invDir = Vector3d(1f / lineDirection.x(), 1f / lineDirection.y(), 1f / lineDirection.z())

        val signDirX = invDir.x() < 0
        val signDirY = invDir.y() < 0
        val signDirZ = invDir.z() < 0

        var bbox = if (signDirX) max else min
        var tmin = (bbox.x() - lineStart.x()) * invDir.x()
        bbox = if (signDirX) min else max
        var tmax = (bbox.x() - lineStart.x()) * invDir.x()
        bbox = if (signDirY) max else min
        val tymin = (bbox.y() - lineStart.y()) * invDir.y()
        bbox = if (signDirY) min else max
        val tymax = (bbox.y() - lineStart.y()) * invDir.y()

        if (tmin > tymax || tymin > tmax) {
            return null
        }
        if (tymin > tmin) {
            tmin = tymin
        }
        if (tymax < tmax) {
            tmax = tymax
        }

        bbox = if (signDirZ) max else min
        val tzmin = (bbox.z() - lineStart.z()) * invDir.z()
        bbox = if (signDirZ) min else max
        val tzmax = (bbox.z() - lineStart.z()) * invDir.z()

        if (tmin > tzmax || tzmin > tmax) {
            return null
        }
        if (tzmin > tmin) {
            tmin = tzmin
        }
        if (tzmax < tmax) {
            tmax = tzmax
        }
        if (tmin < maxDist && tmax > minDist) {

            val intersect = Vector3d(lineStart)

            intersect.add(lineDirection.mul(tmin))
            return intersect
            // return Vector3dm.add(lineStart,
            // lineDirection.clone().normalize().scale(tmin), null);

            // return ray.getPointAtDistance(tmin);
        }
        return null

    }

    fun collidesWith(b: Box): Boolean {
        val overlapX = (b.min.x >= min.x && b.min.x <= max.x) || (min.x >= b.min.x && min.x <= b.max.x)
        val overlapY = (b.min.y >= min.y && b.min.y <= max.y) || (min.y >= b.min.y && min.y <= b.max.y)
        val overlapZ = (b.min.z >= min.z && b.min.z <= max.z) || (min.z >= b.min.z && min.z <= b.max.z)

        return overlapX && overlapY && overlapZ
    }

    fun isPointInside(x: Double, y: Double, z: Double): Boolean = x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z

    fun isPointInside(vec: Vector3dc): Boolean {
        return isPointInside(vec.x(), vec.y(), vec.z())
    }

    override fun toString(): String {
        return "Box(min=$min, max=$max)"
    }

    companion object {
        fun fromExtents(ex: Double, ey: Double, ez: Double): Box {
            return Box(Vector3d(0.0), Vector3d(ex, ey, ez))
        }

        fun fromExtents(extents: Vector3dc) = fromExtents(extents.x(), extents.y(), extents.z())

        fun fromExtentsCenteredHorizontal(ex: Double, ey: Double, ez: Double): Box {
            return Box(Vector3d(-ex * 0.5, 0.0, -ez * 0.5), Vector3d(ex * 0.5, ey, ez * 0.5))
        }

        fun fromExtentsCenteredHorizontal(extents: Vector3dc) = fromExtentsCenteredHorizontal(extents.x(), extents.y(), extents.z())

        fun fromExtents(sx: Double, sy: Double, sz: Double, ex: Double, ey: Double, ez: Double): Box {
            return Box(Vector3d(sx, sy, sz), Vector3d(ex, ey, ez))
        }

        fun fromExtents(min: Vector3dc, max: Vector3dc) = fromExtents(min.x(), min.y(), min.z(), max.x(), max.y(), max.z())
    }
}
