//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.math.random

import org.joml.Vector4f
import org.joml.Vector4fc
import java.util.*

// This class takes care of providing the simplex noise functions for the world
// generator. It is based on other people's work as the comments below show.
/**
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se). Optimisations
 * by Peter Eastman (peastman@drizzle.stanford.edu). Better rank ordering method
 * by Stefan Gustavson in 2012.
 *
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 *
 * Version 2012-03-09
 *
 *
 * This code was placed in the public domain by its original author, Stefan
 * Gustavson. You may use it as you see fit, but attribution is appreciated.
 */
class SeededSimplexNoiseGenerator(seed: String) {
    private val grad3 = arrayOf(Grad(1f, 1f, 0f), Grad((-1f), 1f, 0f), Grad(1f, (-1f), 0f), Grad((-1f), (-1f), 0f), Grad(1f, 0f, 1f), Grad((-1f), 0f, 1f),
            Grad(1f, 0f, (-1f)), Grad((-1f), 0f, (-1f)), Grad(0f, 1f, 1f), Grad(0f, (-1f), 1f), Grad(0f, 1f, (-1f)), Grad(0f, (-1f), (-1f)))
    private val grad4 = arrayOf(
            Grad(0f, 1f, 1f, 1f), Grad(0f, 1f, 1f, (-1f)), Grad(0f, 1f, (-1f), 1f), Grad(0f, 1f, (-1f), (-1f)),
            Grad(0f, (-1f), 1f, 1f), Grad(0f, (-1f), 1f, (-1f)), Grad(0f, (-1f), (-1f), 1f), Grad(0f, (-1f), (-1f), (-1f)),
            Grad(1f, 0f, 1f, 1f), Grad(1f, 0f, 1f, (-1f)), Grad(1f, 0f, (-1f), 1f), Grad(1f, 0f, (-1f), (-1f)),
            Grad((-1f), 0f, 1f, 1f), Grad((-1f), 0f, 1f, (-1f)), Grad((-1f), 0f, (-1f), 1f), Grad((-1f), 0f, (-1f), (-1f)),
            Grad(1f, 1f, 0f, 1f), Grad(1f, 1f, 0f, (-1f)), Grad(1f, (-1f), 0f, 1f), Grad(1f, (-1f), 0f, (-1f)),
            Grad((-1f), 1f, 0f, 1f), Grad((-1f), 1f, 0f, (-1f)), Grad((-1f), (-1f), 0f, 1f), Grad((-1f), (-1f), 0f, (-1f)),
            Grad(1f, 1f, 1f, 0f), Grad(1f, 1f, (-1f), 0f), Grad(1f, (-1f), 1f, 0f), Grad(1f, (-1f), (-1f), 0f),
            Grad((-1f), 1f, 1f, 0f), Grad((-1f), 1f, (-1f), 0f), Grad((-1f), (-1f), 1f, 0f), Grad((-1f), (-1f), (-1f), 0f))

    // To remove the need for index wrapping, float the permutation table length
    var perm = ShortArray(512)
    private val permMod12 = ShortArray(512)

    // Skewing and unskewing factors for 2, 3, and 4 dimensions
    private val F2 = (0.5f * (Math.sqrt(3.0) - 1.0)).toFloat()
    private val G2 = ((3.0f - Math.sqrt(3.0)) / 6.0).toFloat()
    private val F4 = ((Math.sqrt(5.0) - 1.0) / 4.0).toFloat()
    private val G4 = ((5.0f - Math.sqrt(5.0)) / 20.0).toFloat()

    // This method is a *lot* faster than using (int)Math.floor(x)
    private fun fastfloor(x: Float): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }

    private fun dot(g: Grad, x: Float, y: Float): Float {
        return g.x * x + g.y * y
    }

    private fun dot(g: Grad, x: Float, y: Float, z: Float): Float {
        return g.x * x + g.y * y + g.z * z
    }

    private fun dot(g: Grad, x: Float, y: Float, z: Float, w: Float): Float {
        return g.x * x + g.y * y + g.z * z + g.w * w
    }

    fun looped_noise(x: Float, y: Float, period: Float): Float {
        val s = x / period
        val t = y / period
        return looped_noise(s, t, zero, one)
    }

    private val one = Vector4f(1f)
    private val zero = Vector4f(0f)
    fun looped_noise(s: Float, t: Float, offset: Vector4fc, multiplier: Vector4fc): Float {
        val pi = Math.PI.toFloat()
        val nx = Math.cos((s * 2 * pi).toDouble()).toFloat() * multiplier.x() + offset.x()
        val ny = Math.cos((t * 2 * pi).toDouble()).toFloat() * multiplier.y() + offset.y()
        val nz = Math.sin((s * 2 * pi).toDouble()).toFloat() * multiplier.z() + offset.z()
        val nw = Math.sin((t * 2 * pi).toDouble()).toFloat() * multiplier.w() + offset.w()
        return noise(nx, ny, nz, nw)
    }

    fun looped_noise3d(x: Float, y: Float, z: Float, period: Float, x1: Float, y1: Float, x2: Float, y2: Float): Float {
        var z = z
        val s = x / period
        val t = y / period
        z /= period / 15
        val pi = Math.PI.toFloat()
        val nx = (x1 + Math.cos((s * 2 * pi).toDouble()) * x2).toFloat()
        val ny = (y1 + Math.cos((t * 2 * pi).toDouble()) * y2).toFloat()
        val nz = (x1 + Math.sin((s * 2 * pi).toDouble()) * x2).toFloat()
        val nw = (y1 + Math.sin((t * 2 * pi).toDouble()) * y2).toFloat()
        return (noise(nx, ny, z) + noise(nz, nw, z)) / 2
    }

    // 2D simplex noise
    fun noise(xin: Float, yin: Float): Float {
        val n0: Float
        val n1: Float
        val n2: Float // Noise contributions from the three corners
        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin) * F2 // Hairy factor for 2D
        val i = fastfloor(xin + s)
        val j = fastfloor(yin + s)
        val t = (i + j) * G2
        val X0 = i - t // Unskew the cell origin back to (x,y) space
        val Y0 = j - t
        val x0 = xin - X0 // The x,y distances from the cell origin
        val y0 = yin - Y0
        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        val i1: Int
        val j1: Int // Offsets for second (middle) corner of simplex in (i,j)
        // coords
        if (x0 > y0) {
            i1 = 1
            j1 = 0
        } // lower triangle, XY order: (0,0)->(1,0)->(1,1)
        else {
            i1 = 0
            j1 = 1
        } // upper triangle, YX order: (0,0)->(0,1)->(1,1)
        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        val x1 = x0 - i1 + G2 // Offsets for middle corner in (x,y) unskewed
        // coords
        val y1 = y0 - j1 + G2
        val x2 = x0 - 1.0f + 2.0f * G2 // Offsets for last corner in (x,y)
        // unskewed coords
        val y2 = y0 - 1.0f + 2.0f * G2
        // Work out the hashed gradient indices of the three simplex corners
        val ii = i and 255
        val jj = j and 255
        val gi0 = permMod12[ii + perm[jj]].toInt()
        val gi1 = permMod12[ii + i1 + perm[jj + j1]].toInt()
        val gi2 = permMod12[ii + 1 + perm[jj + 1]].toInt()
        // Calculate the contribution from the three corners
        var t0 = 0.5f - x0 * x0 - y0 * y0
        if (t0 < 0) n0 = 0.0f else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0) // (x,y) of grad3 used for
            // 2D gradient
        }
        var t1 = 0.5f - x1 * x1 - y1 * y1
        if (t1 < 0) n1 = 0.0f else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1)
        }
        var t2 = 0.5f - x2 * x2 - y2 * y2
        if (t2 < 0) n2 = 0.0f else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2)
        }
        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0f * (n0 + n1 + n2)
    }

    // 3D simplex noise
    fun noise(xin: Float, yin: Float, zin: Float): Float {
        val n0: Float
        val n1: Float
        val n2: Float
        val n3: Float // Noise contributions from the four corners
        // Skew the input space to determine which simplex cell we're in
        val f3 = (1.0f / 3.0).toFloat()
        val s = (xin + yin + zin) * f3 // Very nice and simple skew factor
        // for 3D
        val i = fastfloor(xin + s)
        val j = fastfloor(yin + s)
        val k = fastfloor(zin + s)
        val g3 = (1.0f / 6.0).toFloat()
        val t = (i + j + k) * g3
        val X0 = i - t // Unskew the cell origin back to (x,y,z) space
        val Y0 = j - t
        val Z0 = k - t
        val x0 = xin - X0 // The x,y,z distances from the cell origin
        val y0 = yin - Y0
        val z0 = zin - Z0
        // For the 3D case, the simplex shape is a slightly irregular
        // tetrahedron.
        // Determine which simplex we are in.
        val i1: Int
        val j1: Int
        val k1: Int // Offsets for second corner of simplex in (i,j,k)
        // coords
        val i2: Int
        val j2: Int
        val k2: Int // Offsets for third corner of simplex in (i,j,k) coords
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            } // X Y Z order
            else if (x0 >= z0) {
                i1 = 1
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 0
                k2 = 1
            } // X Z Y order
            else {
                i1 = 0
                j1 = 0
                k1 = 1
                i2 = 1
                j2 = 0
                k2 = 1
            } // Z X Y order
        } else { // x0<y0
            if (y0 < z0) {
                i1 = 0
                j1 = 0
                k1 = 1
                i2 = 0
                j2 = 1
                k2 = 1
            } // Z Y X order
            else if (x0 < z0) {
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 0
                j2 = 1
                k2 = 1
            } // Y Z X order
            else {
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            } // Y X Z order
        }
        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z),
        // and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z),
        // where
        // c = 1/6.
        val x1 = x0 - i1 + g3 // Offsets for second corner in (x,y,z) coords
        val y1 = y0 - j1 + g3
        val z1 = z0 - k1 + g3
        val x2 = x0 - i2 + 2.0f * g3 // Offsets for third corner in (x,y,z)
        // coords
        val y2 = y0 - j2 + 2.0f * g3
        val z2 = z0 - k2 + 2.0f * g3
        val x3 = x0 - 1.0f + 3.0f * g3 // Offsets for last corner in (x,y,z)
        // coords
        val y3 = y0 - 1.0f + 3.0f * g3
        val z3 = z0 - 1.0f + 3.0f * g3
        // Work out the hashed gradient indices of the four simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val gi0 = permMod12[ii + perm[jj + perm[kk]]].toInt()
        val gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]].toInt()
        val gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]].toInt()
        val gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]].toInt()
        // Calculate the contribution from the four corners
        var t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0
        if (t0 < 0) n0 = 0.0f else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0)
        }
        var t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1
        if (t1 < 0) n1 = 0.0f else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1)
        }
        var t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2
        if (t2 < 0) n2 = 0.0f else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2)
        }
        var t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3
        if (t3 < 0) n3 = 0.0f else {
            t3 *= t3
            n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3)
        }
        // Add contributions from each corner to get the final noise value.
        // The result is scaled to stay just inside [-1,1]
        return 32.0f * (n0 + n1 + n2 + n3)
    }

    // 4D simplex noise, better simplex rank ordering method 2012-03-09
    fun noise(x: Float, y: Float, z: Float, w: Float): Float {
        val n0: Float
        val n1: Float
        val n2: Float
        val n3: Float
        val n4: Float // Noise contributions from the five corners
        // Skew the (x,y,z,w) space to determine which cell of 24 simplices
        // we're in
        val s = (x + y + z + w) * F4 // Factor for 4D skewing
        val i = fastfloor(x + s)
        val j = fastfloor(y + s)
        val k = fastfloor(z + s)
        val l = fastfloor(w + s)
        val t = (i + j + k + l) * G4 // Factor for 4D unskewing
        val X0 = i - t // Unskew the cell origin back to (x,y,z,w) space
        val Y0 = j - t
        val Z0 = k - t
        val W0 = l - t
        val x0 = x - X0 // The x,y,z,w distances from the cell origin
        val y0 = y - Y0
        val z0 = z - Z0
        val w0 = w - W0
        // For the 4D case, the simplex is a 4D shape I won't even try to
        // describe.
        // To find out which of the 24 possible simplices we're in, we need to
        // determine the magnitude ordering of x0, y0, z0 and w0.
        // Six pair-wise comparisons are performed between each possible pair
        // of the four coordinates, and the results are used to rank the
        // numbers.
        var rankx = 0
        var ranky = 0
        var rankz = 0
        var rankw = 0
        if (x0 > y0) rankx++ else ranky++
        if (x0 > z0) rankx++ else rankz++
        if (x0 > w0) rankx++ else rankw++
        if (y0 > z0) ranky++ else rankz++
        if (y0 > w0) ranky++ else rankw++
        if (z0 > w0) rankz++ else rankw++
        val i1: Int
        val j1: Int
        val k1: Int
        val l1: Int // The integer offsets for the second simplex corner
        val i2: Int
        val j2: Int
        val k2: Int
        val l2: Int // The integer offsets for the third simplex corner
        val i3: Int
        val j3: Int
        val k3: Int
        val l3: Int // The integer offsets for the fourth simplex corner
        // simplex[c] is a 4-vector with the numbers 0, 1, 2 and 3 in some
        // order.
        // Many values of c will never occur, since e.g. x>y>z>w makes x<z, y<w
        // and x<w
        // impossible. Only the 24 indices which have non-zero entries make any
        // sense.
        // We use a thresholding to set the coordinates in turn from the largest
        // magnitude.
        // Rank 3 denotes the largest coordinate.
        i1 = if (rankx >= 3) 1 else 0
        j1 = if (ranky >= 3) 1 else 0
        k1 = if (rankz >= 3) 1 else 0
        l1 = if (rankw >= 3) 1 else 0
        // Rank 2 denotes the second largest coordinate.
        i2 = if (rankx >= 2) 1 else 0
        j2 = if (ranky >= 2) 1 else 0
        k2 = if (rankz >= 2) 1 else 0
        l2 = if (rankw >= 2) 1 else 0
        // Rank 1 denotes the second smallest coordinate.
        i3 = if (rankx >= 1) 1 else 0
        j3 = if (ranky >= 1) 1 else 0
        k3 = if (rankz >= 1) 1 else 0
        l3 = if (rankw >= 1) 1 else 0
        // The fifth corner has all coordinate offsets = 1, so no need to
        // compute that.
        val x1 = x0 - i1 + G4 // Offsets for second corner in (x,y,z,w)
        // coords
        val y1 = y0 - j1 + G4
        val z1 = z0 - k1 + G4
        val w1 = w0 - l1 + G4
        val x2 = x0 - i2 + 2.0f * G4 // Offsets for third corner in (x,y,z,w)
        // coords
        val y2 = y0 - j2 + 2.0f * G4
        val z2 = z0 - k2 + 2.0f * G4
        val w2 = w0 - l2 + 2.0f * G4
        val x3 = x0 - i3 + 3.0f * G4 // Offsets for fourth corner in
        // (x,y,z,w) coords
        val y3 = y0 - j3 + 3.0f * G4
        val z3 = z0 - k3 + 3.0f * G4
        val w3 = w0 - l3 + 3.0f * G4
        val x4 = x0 - 1.0f + 4.0f * G4 // Offsets for last corner in (x,y,z,w)
        // coords
        val y4 = y0 - 1.0f + 4.0f * G4
        val z4 = z0 - 1.0f + 4.0f * G4
        val w4 = w0 - 1.0f + 4.0f * G4
        // Work out the hashed gradient indices of the five simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val ll = l and 255
        val gi0 = perm[ii + perm[jj + perm[kk + perm[ll]]]] % 32
        val gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1 + perm[ll + l1]]]] % 32
        val gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2 + perm[ll + l2]]]] % 32
        val gi3 = perm[ii + i3 + perm[jj + j3 + perm[kk + k3 + perm[ll + l3]]]] % 32
        val gi4 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1 + perm[ll + 1]]]] % 32
        // Calculate the contribution from the five corners
        var t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0
        if (t0 < 0) n0 = 0.0f else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad4[gi0], x0, y0, z0, w0)
        }
        var t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1
        if (t1 < 0) n1 = 0.0f else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad4[gi1], x1, y1, z1, w1)
        }
        var t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2
        if (t2 < 0f) n2 = 0.0f else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad4[gi2], x2, y2, z2, w2)
        }
        var t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3
        if (t3 < 0f) n3 = 0.0f else {
            t3 *= t3
            n3 = t3 * t3 * dot(grad4[gi3], x3, y3, z3, w3)
        }
        var t4 = 0.6f - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4
        if (t4 < 0f) n4 = 0.0f else {
            t4 *= t4
            n4 = t4 * t4 * dot(grad4[gi4], x4, y4, z4, w4)
        }
        // Sum up and scale the result to cover the range [-1,1]
        return 27.0f * (n0 + n1 + n2 + n3 + n4)
    }

    // Inner class to speed upp gradient computations
    // (array access is a lot slower than member access)
    private class Grad {
        var x: Float
        var y: Float
        var z: Float
        var w = 0f

        internal constructor(x: Float, y: Float, z: Float) {
            this.x = x
            this.y = y
            this.z = z
        }

        internal constructor(x: Float, y: Float, z: Float, w: Float) {
            this.x = x
            this.y = y
            this.z = z
            this.w = w
        }
    }

    init {
        // Init p array based on seed
        val p = ShortArray(256)
        val temp: MutableList<Short> = ArrayList()
        for (i in 0..255) {
            temp.add(i.toShort())
        }
        val seedBytes = seed.toByteArray()
        for (i in 0..255) {
            val select = seedBytes[i % seedBytes.size].toInt()
            if (select < temp.size) {
                p[i] = temp[select]
                temp.removeAt(select)
            } else {
                p[i] = temp[0]
                temp.removeAt(0)
            }
        }
        // Init perm arrays
        for (i in 0..511) {
            perm[i] = p[i and 255]
            permMod12[i] = (perm[i] % 12).toShort()
        }
    }
}