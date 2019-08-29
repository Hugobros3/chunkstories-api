//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import org.joml.*
import xyz.chunkstories.api.graphics.structs.Camera
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f
import java.lang.Math

/** Leftover from the old renderin code, bound for some massive improvements in the not too distant future */
class Frustrum(val camera: Camera) {
    //var corners : Array<Vector3f>
    var cameraPlanes: Array<Plane3d> = Array(6) { Plane3d(Vector3d(), Vector3d(), Vector3d()) }

    init {
        computeFrustrumPlanes()
    }

    private fun computeFrustrumPlanes() {
        /*var temp: Vector3f

        val cameraPosition = camera.position
        //val cameraPosition = Vector3f(0f)

        val fov = camera.fov
        val viewportWidth = window.width
        val viewportHeight = window.height

        val tang = Math.tan(fov / 2.0).toFloat()
        val ratio = viewportWidth.toFloat() / viewportHeight.toFloat()
        val nh = 0.1f * tang
        val nw = nh * ratio
        val fh = 2000f * tang
        val fw = fh * ratio

        val lookAt = Vector3f(camera.lookingAt).add(cameraPosition)
        //val up = Vector3f(0.0f, 1.0f, 0.0f)
        val up = camera.up

        // Create the 6 frustrum planes
        val Z = Vector3f(cameraPosition.x(), cameraPosition.y(), cameraPosition.z())

        Z.sub(lookAt)
        Z.normalize()

        val X = Vector3f()

        up.cross(Z, X)
        X.normalize()

        val Y = Vector3f()
        Z.cross(X, Y)

        val nearCenterPoint = Vector3f(cameraPosition.x() as Float, cameraPosition.y() as Float, cameraPosition.z() as Float)
        temp = Vector3f(Z)
        temp.mul(0.1f)

        nearCenterPoint.sub(temp)

        val farCenterPoint = Vector3f(cameraPosition.x() as Float, cameraPosition.y() as Float, cameraPosition.z() as Float)
        temp = Vector3f(Z)
        temp.mul(2000f)

        farCenterPoint.sub(temp)*/

        /*val nearTopLeft = nearCenterPoint.plus(Y.times(nh).minus(X.times(nw)))
        val nearTopRight = nearCenterPoint.plus(Y.times(nh).plus(X.times(nw)))
        val nearBottomLeft = nearCenterPoint.minus(Y.times(nh).plus(X.times(nw)))
        val nearBottomRight = nearCenterPoint.minus(Y.times(nh).minus(X.times(nw)))

        val farTopLeft = farCenterPoint.plus(Y.times(fh).minus(X.times(fw)))
        val farTopRight = farCenterPoint.plus(Y.times(fh).plus(X.times(fw)))
        val farBottomLeft = farCenterPoint.minus((Y * fh).plus(X.times(fw)))
        val farBottomRight = farCenterPoint - ((Y * fh) - (X * fw))*/

        /*val ntl = Vector4f(-1.0f, 1.0f, 0.0f ,1.0f)
        val ntr = Vector4f(1.0f, 1.0f, 0.0f ,1.0f)
        val nbl = Vector4f(-1.0f, -1.0f, 0.0f ,1.0f)
        val nbr = Vector4f(1.0f, -1.0f, 0.0f ,1.0f)

        val ftl = Vector4f(-1.0f, 1.0f, 1.0f ,1.0f)
        val ftr = Vector4f(1.0f, 1.0f, 1.0f ,1.0f)
        val fbl = Vector4f(-1.0f, -1.0f, 1.0f ,1.0f)
        val fbr = Vector4f(1.0f, -1.0f, 1.0f ,1.0f)*/

        val ntl = Vector4f(-1.0f, 1.0f, 1.0f, 1.0f)
        val ntr = Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
        val nbl = Vector4f(-1.0f, -1.0f, 1.0f, 1.0f)
        val nbr = Vector4f(1.0f, -1.0f, 1.0f, 1.0f)

        val ftl = Vector4f(-1.0f, 1.0f, 0.0f, 1.0f)
        val ftr = Vector4f(1.0f, 1.0f, 0.0f, 1.0f)
        val fbl = Vector4f(-1.0f, -1.0f, 0.0f, 1.0f)
        val fbr = Vector4f(1.0f, -1.0f, 0.0f, 1.0f)

        fun transform(a: Vector4fc): Vector3f {
            val t = Vector4f(a)
            camera.projectionMatrixInverted.transform(t, t)
            camera.viewMatrixInverted.transform(t, t)
            t.mul(1f / t.w)
            return Vector3f(t.x, t.y, t.z)
        }

        val nearTopLeft = transform(ntl).toVec3d()
        val nearTopRight = transform(ntr).toVec3d()
        val nearBottomLeft = transform(nbl).toVec3d()
        val nearBottomRight = transform(nbr).toVec3d()

        val farTopLeft = transform(ftl).toVec3d()
        val farTopRight = transform(ftr).toVec3d()
        val farBottomLeft = transform(fbl).toVec3d()
        val farBottomRight = transform(fbr).toVec3d()

        /*println("fbr: ${farTopLeft.x()} ${farTopLeft.y()} ${farTopLeft.z()}")
        val a = ftl
        camera.projectionMatrixInverted.transform(a, a)
        camera.viewMatrixInverted.transform(a, a)
        a.mul(1f / a.w)
        println("a: ${a.x()} ${a.y()} ${a.z()}")

        val c = farTopLeft
        val d = Vector4f(c.x, c.y, c.z, 1f)

        camera.viewMatrix.transform(d, d)
        camera.projectionMatrix.transform(d, d)
        d.mul(1f / d.w)
        println("dd: ${d.x()} ${d.y()} ${d.z()}")*/

        cameraPlanes[0] = Plane3d(nearTopRight, nearTopLeft, farTopLeft)
        cameraPlanes[1] = Plane3d(nearBottomLeft, nearBottomRight, farBottomRight)
        cameraPlanes[2] = Plane3d(nearTopLeft, nearBottomLeft, farBottomLeft)
        cameraPlanes[3] = Plane3d(nearBottomRight, nearTopRight, farBottomRight)
        cameraPlanes[4] = Plane3d(nearTopLeft, nearTopRight, nearBottomRight)
        cameraPlanes[5] = Plane3d(farTopRight, farTopLeft, farBottomLeft)
    }

    fun isBoxInFrustrum(box: Box): Boolean {
        /*val frustrumCheckBoxOrigin = Vector3f()
        frustrumCheckBoxOrigin.set((box.xPosition + box.xWidth / 2).toFloat(), (box.yPosition + box.yHeight / 2).toFloat(), (box.zPosition + box.zWidth / 2).toFloat())

        val frustrumCheckBoxSize = Vector3f()
        frustrumCheckBoxSize.set(box.xWidth.toFloat(), box.yHeight.toFloat(), box.zWidth.toFloat())*/

        return this.isBoxInFrustrum(box.min, box.max)
    }

    private fun isBoxInFrustrum(min: Vector3dc, max: Vector3dc): Boolean {
        val corners = arrayOf(Vector3d(
                // i=0 j=0 k=0
                min.x(),
                min.y(),
                min.z()
        ), Vector3d(
                // i=0 j=0 k=1
                min.x(),
                min.y(),
                max.z()
        ), Vector3d(
                // i=0 j=1 k=0
                min.x(),
                max.x(),
                min.z()
        ), Vector3d(
                // i=0 j=1 k=1
                min.x(),
                max.y(),
                max.z()
        ), Vector3d(
                // i=1 j=0 k=0
                max.x(),
                min.y(),
                min.z()
        ), Vector3d(
                // i=1 j=0 k=1
                max.x(),
                min.y(),
                max.z()
        ), Vector3d(
                // i=1 j=1 k=0
                max.x(),
                max.y(),
                min.y()
        ), Vector3d(
                // i=1 j=1 k=1
                max.x(),
                max.y(),
                max.z()
        )
        )

        for (i in 0..5) {
            var out = 0
            var `in` = 0
            for (c in 0..7) {
                // System.out.println(i+" "+c+" "+cameraPlanes[i].distance(corners[c]) + "
                // center "+center);
                if (cameraPlanes[i].distance(corners[c]) < 0)
                    out++
                else
                    `in`++
            }
            if (`in` == 0) {
                // System.out.println("Rejected "+center+" on plane "+i);
                return false
            } else if (out > 0) {
                // Partially occluded.
                // return false;
            }
        }
        return true
    }

}

operator fun Vector3f.minus(b: Vector3f): Vector3f {
    val out = Vector3f(this)
    out.sub(b)
    return out
}

operator fun Vector3f.times(scale: Float): Vector3f {
    val out = Vector3f(this)
    out.mul(scale)
    return out
}

operator fun Vector3f.plus(b: Vector3f): Vector3f {
    val out = Vector3f(this)
    out.add(b)
    return out
}

private fun toRad(d: Double): Double {
    return d / 360 * 2.0 * Math.PI
}