//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.math

import org.joml.Matrix4f
import org.joml.Vector3d

/** Hopefully hi-performance matrix class, replacing and using bits from LWJGL2
 * utility library  */
class Quaternion4d {
    var s: Double
    var v: Vector3d

    constructor() {
        s = 0.0
        v = Vector3d(0.0)
    }

    constructor(s: Double, v: Vector3d?) {
        this.s = s
        this.v = Vector3d(v)
    }

    constructor(quat: Quaternion4d) {
        s = quat.s
        v = Vector3d(quat.v)
    }

    fun add(quat: Quaternion4d): Quaternion4d {
        s += quat.s
        v.add(quat.v)
        return this
    }

    fun sub(quat: Quaternion4d): Quaternion4d {
        s -= quat.s
        v.sub(quat.v)
        return this
    }

    fun scale(scalar: Double): Quaternion4d {
        s *= scalar
        v.mul(scalar)
        return this
    }

    fun mult(quat: Quaternion4d): Quaternion4d {
        return mult(this, quat, null)
    }

    fun conjugate(): Quaternion4d {
        return Quaternion4d(s, Vector3d(v).negate())
    }

    fun norm(): Double {
        return Math.sqrt(s * s + v.dot(v) * v.dot(v))
        // return Math.sqrt(s * s + Vector3d.dot(v, v) * Vector3d.dot(v, v));
    }

    fun normalize(): Quaternion4d {
        scale(1.0 / norm())
        return this
    }

    fun inverse(): Quaternion4d {
        val inverse = conjugate()
        inverse.scale(1.0 / (norm() * norm()))
        return inverse
    }

    fun toMatrix4f(): Matrix4f {
        val matrix = Matrix4f()
        val n = v.x * v.x + v.y * v.y + v.z * v.z + s * s
        val ss: Double = if (n > 0.0) 2.0 / n else 0.0
        val xs = v.x * ss
        val ys = v.y * ss
        val zs = v.z * ss
        val wx = s * xs
        val wy = s * ys
        val wz = s * zs
        val xx = v.x * xs
        val xy = v.x * ys
        val xz = v.x * zs
        val yy = v.y * ys
        val yz = v.y * zs
        val zz = v.z * zs
        matrix.m00((1.0f - (yy + zz)).toFloat())
        matrix.m10((xy - wz).toFloat())
        matrix.m20((xz + wy).toFloat())
        matrix.m01((xy + wz).toFloat())
        matrix.m11((1.0f - (xx + zz)).toFloat())
        matrix.m21((yz - wx).toFloat())
        matrix.m02((xz - wy).toFloat())
        matrix.m12((yz + wx).toFloat())
        matrix.m22((1.0f - (xx + yy)).toFloat())
        return matrix
    }

    override fun toString(): String {
        return "[Quaternion4d s:$s v:$v]"
    }

    fun clone(): Quaternion4d {
        return Quaternion4d(this)
    }

    companion object {
        fun mult(a: Quaternion4d, b: Quaternion4d, out: Quaternion4d?): Quaternion4d {
            var out = out
            if (out == null) out = Quaternion4d()

            // [ Sa.Sb - a.b ,
            out.s = a.s * b.s - a.v.dot(b.v)
            // Sa.b + Sb.a + a x b ]
            val aBv = Vector3d(b.v).mul(a.s)
            val vBa = Vector3d(a.v).mul(b.s)
            out.v = aBv.add(vBa).add(a.v.cross(b.v))
            // out.v = Vector3d.add(aBv, vBa, null).add(Vector3d.cross(a.v, b.v));
            return out
        }

        fun dot(a: Quaternion4d, b: Quaternion4d): Double {
            return a.s * b.s + a.v.x * b.v.x + a.v.y * b.v.y + a.v.z * b.v.z
        }

        fun fromAxisAngle(axis: Vector3d?, angle: Double): Quaternion4d {
            var angle = angle
            angle /= 2.0
            return Quaternion4d(Math.cos(angle), Vector3d(axis).mul(Math.sin(angle)))
        }

        fun rotate(vector: Vector3d?, axis: Vector3d?, angle: Double): Vector3d {
            // Make quaternion out of the vector
            val p = Quaternion4d(0.0, vector)

            // Make a rotation quaternion about the vector
            val q = fromAxisAngle(axis, angle)

            // Apply rotation
            val iq = q.inverse()
            // qpq^-1
            return q.mult(p).mult(iq).v
        }

        fun slerp(q1: Quaternion4d, q2: Quaternion4d, t: Double): Quaternion4d {
            // q1.normalize();
            // q2.normalize();
            var dot = dot(q1, q2)

            // Ignore cases where the two Quaternion4d are identical
            if (dot <= -1.0 || dot >= 1.0) {
                return q1
            }

            // No interp
            if (t <= 0.0) return q1

            // No interp
            if (t >= 1.0) return q2

            // Avoid taking the long path
            if (dot < 0.0) {
                dot *= -1.0
                q1.scale(-1.0)
            }

            // Get the angle
            val omega = Math.acos(dot)

            // Compute numerators and denominators
            val sinOmega = Math.sin(omega)
            val sinOmegaT = Math.sin(omega * t)
            val sinOmega1_T = Math.sin(omega * (1.0 - t))

            // Security :
            if (sinOmega == 0.0) {
                return q1
            }

            // Scale vectors to fractions
            val left = Quaternion4d(q1)
            left.scale(sinOmega1_T / sinOmega)
            val right = Quaternion4d(q2)
            right.scale(sinOmegaT / sinOmega)

            // Add vectors
            val result = Quaternion4d()
            result.add(left)
            result.add(right)
            return result
        }
    }
}