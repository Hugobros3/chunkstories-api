//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import org.joml.Vector3d
import org.joml.Vector3dc

class Plane3d(p1: Vector3dc, p2: Vector3dc, p3: Vector3dc) {
    internal val a: Double
    internal val b: Double
    internal val c: Double
    internal val d: Double
    internal val n: Vector3d

    init {
        val v = Vector3d(p2)
        val u = Vector3d(p3)

        val p1d = Vector3d(p1)

        v.sub(p1d)
        u.sub(p1d)

        n = Vector3d()

        v.cross(u, n)
        n.normalize()

        a = n.x()
        b = n.y()
        c = n.z()

        d = -p1d.dot(n)
    }

    fun distance(point: Vector3dc): Double {
        return a * point.x() + b * point.y() + c * point.z() + d
    }
}