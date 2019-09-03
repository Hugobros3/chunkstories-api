//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.math

import org.joml.Vector3d
import org.joml.Vector3f
import org.junit.Test
import xyz.chunkstories.api.physics.Plane3d

class TestPlane {
    @Test
    fun testPlane() {
        val p1 = Vector3d(0.0, 0.0, 0.0)
        val p2 = Vector3d(1.0, 0.0, 0.0)
        val p3 = Vector3d(0.0, 0.0, 1.0)

        val t = Vector3d(0.0, 1.0, 0.0)
        val plane = Plane3d(p1, p2, p3)

        val d = plane.distance(t)
        println(d)
        println(plane.n)
    }
}