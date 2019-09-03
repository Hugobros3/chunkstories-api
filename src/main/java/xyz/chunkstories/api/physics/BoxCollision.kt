//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import org.joml.Vector3d
import org.joml.Vector3dc
import java.lang.Double.min
import kotlin.math.absoluteValue

fun distanceToIntersection(a: Double, b: Double, c: Double, d: Double, v: Double): Double {
    if(v == 0.0)
        return Double.NaN
    else if(v > 0.0) {
        return c - b
    } else {
        return a - d
    }

    return Double.NaN
}

//inline fun safemin(a: Double, b: Double) = if(a.isNaN()) b else if(b.isNaN()) a else min(a, b)

sealed class BoxIntersectionQueryResult {
    object None : BoxIntersectionQueryResult()
    data class Collision(val with: Box, val t: Double, val normal: Vector3dc) : BoxIntersectionQueryResult()
}

fun distanceToIntersection(a: Box, b: Box, v: Vector3dc, tMax: Double): BoxIntersectionQueryResult {
    var tmin = Double.NaN
    val t0x = distanceToIntersection(a.min.x, a.max.x, b.min.x, b.max.x, v.x())

    var dim = -1
    if(!t0x.isNaN() && t0x >= 0.0 && t0x <= tMax) {
        val ta = Box(a)
        val vs = Vector3d(v).mul(t0x / v.x().absoluteValue)
        ta.translate(vs)
        if(touches(ta, b)) {
            tmin = t0x
            dim = 0
        }
    }

    val t0y = distanceToIntersection(a.min.y, a.max.y, b.min.y, b.max.y, v.y())
    if(!t0y.isNaN() && t0y >= 0.0 && t0y <= tMax) {
        val ta = Box(a)
        val vs = Vector3d(v).mul(t0y / v.y().absoluteValue)
        ta.translate(vs)
        if(touches(ta, b)) {
            if(tmin.isNaN() || t0y < tmin) {
                tmin = t0y
                dim = 1
            }
    //            tmin = safemin(tmin, t0y)
        }
    }

    val t0z = distanceToIntersection(a.min.z, a.max.z, b.min.z, b.max.z, v.z())
    if(!t0z.isNaN() && t0z >= 0.0 && t0z <= tMax) {
        val ta = Box(a)
        val vs = Vector3d(v).mul(t0z / v.z().absoluteValue)
        ta.translate(vs)
        if(touches(ta, b)) {
            if(tmin.isNaN() || t0z < tmin) {
                tmin = t0z
                dim = 2
            }
            //tmin = safemin(tmin, t0z)
        }
    }

    if(tmin.isNaN())
        return BoxIntersectionQueryResult.None
    else {
        val normal = when(dim) {
            0 -> if (v.x() < 0) Vector3d(1.0, 0.0, 0.0) else Vector3d(-1.0, 0.0, 0.0)
            1 -> if (v.y() < 0) Vector3d(0.0, 1.0, 0.0) else Vector3d(0.0, -1.0, 0.0)
            2 -> if (v.z() < 0) Vector3d(0.0, 0.0, 1.0) else Vector3d(0.0, 0.0, -1.0)
            else -> throw Exception("Impossible dimension")
        }
        return BoxIntersectionQueryResult.Collision(b, tmin, normal)
    }
}

fun touches(a: Box, b: Box): Boolean {
    return !(
            a.max.x < b.min.x ||
                    a.max.y < b.min.y ||
                    a.max.z < b.min.z ||
                    a.min.x > b.max.x ||
                    a.min.y > b.max.y ||
                    a.min.z > b.max.z
            )
}

fun overlaps(a: Box, b: Box): Boolean {
    return !(
            a.max.x <= b.min.x ||
                    a.max.y <= b.min.y ||
                    a.max.z <= b.min.z ||
                    a.min.x >= b.max.x ||
                    a.min.y >= b.max.y ||
                    a.min.z >= b.max.z
            )
}