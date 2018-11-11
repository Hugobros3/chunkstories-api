package io.xol.chunkstories.api.physics

import org.joml.Vector3f
import org.joml.Vector3d

class Plane(p1: Vector3f, p2: Vector3f, p3: Vector3f) {
    val a: Double
    val b: Double
    val c: Double
    val d: Double
    internal var n: Vector3d

    init {
        val v = Vector3d(p2.x().toDouble(), p2.y().toDouble(), p2.z().toDouble())
        val u = Vector3d(p3.x().toDouble(), p3.y().toDouble(), p3.z().toDouble())

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

    fun distance(point: Vector3f): Double {
        return a * point.x() + b * point.y() + c * point.z() + d
    }
}