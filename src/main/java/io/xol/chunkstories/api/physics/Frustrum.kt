package io.xol.chunkstories.api.physics

import io.xol.chunkstories.api.graphics.Window
import io.xol.chunkstories.api.graphics.structs.Camera
import org.joml.Vector3f
import org.joml.Vector3fc

/** Leftover from the old renderin code, bound for some massive improvements in the not too distant future */
class Frustrum(val camera: Camera, val window: Window) {
    var corners : Array<Vector3f>
    var cameraPlanes : Array<Plane>

    init {
        corners = Array(8) { Vector3f() }
        cameraPlanes = Array(6) { Plane(Vector3f(), Vector3f(), Vector3f())}

        computeFrustrumPlanes()
    }

    private fun computeFrustrumPlanes() {
        var temp: Vector3f

        val cameraPosition = camera.position
        //val cameraPosition = Vector3f(0f)

        //TODO make this correctly
        //Hint: take cardinal points in projected space and unproject them
        val fov = camera.fov
        val viewportWidth = window.width
        val viewportHeight = window.height

        val tang = Math.tan(fov / 2.0).toFloat()
        val ratio = viewportWidth.toFloat() / viewportHeight.toFloat()
        val nh = 0.1f * tang
        val nw = nh * ratio
        val fh = 3000f * tang
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
        temp.mul(3000f)

        farCenterPoint.sub(temp)

        // Eventually the fucking points
        val nearTopLeft = nearCenterPoint.plus(Y.times(nh).minus(X.times(nw)))
        val nearTopRight = nearCenterPoint.plus(Y.times(nh).plus(X.times(nw)))
        val nearBottomLeft = nearCenterPoint.minus(Y.times(nh).plus(X.times(nw)))
        val nearBottomRight = nearCenterPoint.minus(Y.times(nh).minus(X.times(nw)))

        val farTopLeft = farCenterPoint.plus(Y.times(fh).minus(X.times(fw)))
        val farTopRight = farCenterPoint.plus(Y.times(fh).plus(X.times(fw)))
        val farBottomLeft = farCenterPoint.minus((Y * fh).plus(X.times(fw)))
        val farBottomRight = farCenterPoint - ((Y * fh) - (X * fw))

        cameraPlanes[0] = Plane(nearTopRight, nearTopLeft, farTopLeft)
        cameraPlanes[1] = Plane(nearBottomLeft, nearBottomRight, farBottomRight)
        cameraPlanes[2] = Plane(nearTopLeft, nearBottomLeft, farBottomLeft)
        cameraPlanes[3] = Plane(nearBottomRight, nearTopRight, farBottomRight)
        cameraPlanes[4] = Plane(nearTopLeft, nearTopRight, nearBottomRight)
        cameraPlanes[5] = Plane(farTopRight, farTopLeft, farBottomLeft)
    }

    fun isBoxInFrustrum(box: Box): Boolean {
        val frustrumCheckBoxOrigin = Vector3f()
        frustrumCheckBoxOrigin.set((box.xPosition + box.xWidth / 2).toFloat(), (box.yPosition + box.yHeight / 2).toFloat(), (box.zPosition + box.zWidth / 2).toFloat())

        val frustrumCheckBoxSize = Vector3f()
        frustrumCheckBoxSize.set(box.xWidth.toFloat(), box.yHeight.toFloat(), box.zWidth.toFloat())

        return this.isBoxInFrustrum(frustrumCheckBoxOrigin, frustrumCheckBoxSize)
    }

    fun isBoxInFrustrum(center: Vector3fc, dimensions: Vector3fc): Boolean {

        val PLUSONE = 0.5f
        val MINUSONE = -0.5f

        // i=0 j=0 k=0
        corners[0].x = center.x() + dimensions.x() * MINUSONE
        corners[0].y = center.y() + dimensions.y() * MINUSONE
        corners[0].z = center.z() + dimensions.z() * MINUSONE
        // i=0 j=0 k=1
        corners[1].x = center.x() + dimensions.x() * MINUSONE
        corners[1].y = center.y() + dimensions.y() * MINUSONE
        corners[1].z = center.z() + dimensions.z() * PLUSONE
        // i=0 j=1 k=0
        corners[2].x = center.x() + dimensions.x() * MINUSONE
        corners[2].y = center.y() + dimensions.y() * PLUSONE
        corners[2].z = center.z() + dimensions.z() * MINUSONE
        // i=0 j=1 k=1
        corners[3].x = center.x() + dimensions.x() * MINUSONE
        corners[3].y = center.y() + dimensions.y() * PLUSONE
        corners[3].z = center.z() + dimensions.z() * PLUSONE
        // i=1 j=0 k=0
        corners[4].x = center.x() + dimensions.x() * PLUSONE
        corners[4].y = center.y() + dimensions.y() * MINUSONE
        corners[4].z = center.z() + dimensions.z() * MINUSONE
        // i=1 j=0 k=1
        corners[5].x = center.x() + dimensions.x() * PLUSONE
        corners[5].y = center.y() + dimensions.y() * MINUSONE
        corners[5].z = center.z() + dimensions.z() * PLUSONE
        // i=1 j=1 k=0
        corners[6].x = center.x() + dimensions.x() * PLUSONE
        corners[6].y = center.y() + dimensions.y() * PLUSONE
        corners[6].z = center.z() + dimensions.z() * MINUSONE
        // i=1 j=1 k=1
        corners[7].x = center.x() + dimensions.x() * PLUSONE
        corners[7].y = center.y() + dimensions.y() * PLUSONE
        corners[7].z = center.z() + dimensions.z() * PLUSONE

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