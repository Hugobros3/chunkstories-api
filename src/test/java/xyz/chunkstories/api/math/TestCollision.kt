package xyz.chunkstories.api.math

import org.joml.Vector3d
import org.junit.Test
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.physics.distanceToIntersection

class TestCollision {
    @Test
    fun test1d() {
        val ta = distanceToIntersection(0.0, 2.0, 6.0, 7.0, 1.0)
        println("ta: $ta")

        val tb = distanceToIntersection(0.0, 2.0, -12.0, -10.0, -1.0)
        println("tb: $tb")

        val tnot = distanceToIntersection(0.0, 2.0, -12.0, -10.0, 1.0)
        println("tnot: $tnot")
    }

    @Test
    fun testBox() {
        val a = Box.fromExtents(2.0, 1.0, 1.0)

        val b = Box.fromExtents(2.0, 1.0, 1.0).translate(10.0, 0.0, 0.0)
        val v = Vector3d(1.0, 0.0, 0.0).normalize()

        val t = distanceToIntersection(a, b, v, 100.0)
        println("t: $t")

        println("t wrong dir: ${distanceToIntersection(a, b, Vector3d(-1.0, 0.0, 0.0).normalize(), 100.0)}")
        println("t self: ${distanceToIntersection(a, a, Vector3d(-1.0, 0.0, 0.0).normalize(), 100.0)}")
    }
}