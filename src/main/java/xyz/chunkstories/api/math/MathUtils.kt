//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.math

import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.Exception

/** Helper class to do crappy simple math */
object MathUtils {
    fun ceil(x: Double): Int {
        val xi = x.toInt()
        return if (x > xi) xi + 1 else xi
    }
    fun ceil(x: Float): Int {
        val xi = x.toInt()
        return if (x > xi) xi + 1 else xi
    }

    fun floor(x: Double): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }
    fun floor(x: Float): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }

    fun mixd(a: Double, b: Double, f: Double): Double = a * (1.0 - f) + b * f
    fun mixf(a: Float, b: Float, f: Float): Float = a * (1.0f - f) + b * f

    // Can't be expressed in Kotlin currently.
    //inline fun <reified T: Number>mix(left: T, right: T, alpha: T) = left * (1.0 - alpha) + right * alpha

    fun mix(a: Vector3f, b: Vector3f, f: Float): Vector3f {
        val vec = Vector3f()
        vec.x = mixf(a.x, b.x, f)
        vec.y = mixf(a.y, b.y, f)
        vec.z = mixf(a.z, b.z, f)
        return vec
    }

    fun mix(a: Vector3d, b: Vector3d, f: Double): Vector3d {
        val vec = Vector3d()
        vec.x = mixd(a.x, b.x, f)
        vec.y = mixd(a.y, b.y, f)
        vec.z = mixd(a.z, b.z, f)
        return vec
    }

    fun clampi(a: Int, min: Int, max: Int): Int = clamp<Int>(a, min, max)
    fun clampd(a: Double, l: Double, u: Double): Double = clamp<Double>(a, l, u)
    fun clampf(a: Float, l: Float, u: Float): Float = clamp<Float>(a, l, u)

    inline fun <reified T: Comparable<T>>clamp(v: T, min: T, max: T)
        = if (v < min) min else if (v > max) max else v

    inline fun <reified T: Comparable<T>>min(a: T, b: T)
        = if (a <= b) a else b

    inline fun <reified T: Comparable<T>>max(a: T, b: T)
        = if (a >= b) a else b

    // Hack because Kotlin's reified generics are not smart enough to optimize checks such as T::class == Float::class
    // and leave a huge runtime overhead instead
    inline fun <reified T> is_float(): Boolean = 1.0f as? T != null
    inline fun <reified T> is_double(): Boolean = 1.0 as? T != null
    inline fun <reified T> is_int(): Boolean = 1 as? T != null

    inline fun <reified T: Number>soft_cast(v: Number): T = when {
        is_float<T>() -> v.toFloat() as T
        is_double<T>() -> v.toDouble() as T
        is_int<T>() -> v.toInt() as T
        /*T::class == Float::class -> v.toFloat() as T
        T::class == Double::class -> v.toDouble() as T
        T::class == Int::class -> v.toInt() as T*/
        else -> throw Exception("Unsupported type ${T::class.simpleName}")
    }

    // slower experimental version that has a runtime branch overhead
    inline fun <reified T: Number>rnd_uniform_() = soft_cast<T>(Math.random())

    /** Returns a number between 0 and 1 (excluded) */
    fun rnd_uniformf() = Math.random().toFloat()
    /** Returns a number between 0 and 1 (excluded) */
    fun rnd_uniformd() = Math.random()

    // slower experimental version that has a runtime branch overhead
    inline fun <reified T: Comparable<T>> abs_(v: T): T {
        return when {
            is_float<T>() -> Math.abs(v as Float) as T
            is_double<T>() -> Math.abs(v as Double) as T
            is_int<T>() -> Math.abs(v as Int) as T
            else -> throw Exception("Unsupported type ${T::class.simpleName}")
        }
    }

    fun mod_dist(p0: Int, p1: Int, mod: Int): Int {
        val a = p0 % mod
        val b = p1 % mod
        // | a    b |
        //   <---->
        val d1 = Math.abs(b - a)
        // | _    b | a
        //        <--->
        val d2 = Math.abs(b - (a + mod))
        return min(d1, d2)
        /*var a = a
        var b = b
        a %= mod
        b %= mod
        if (a < 0) a += mod
        if (b < 0) b += mod
        val mx = Math.min(a, b)
        val mn = Math.max(a, b)
        return Math.min(Math.abs(mn - mx), Math.abs(mx - (mn - mod)))*/
    }

    fun mod_dist(p0: Double, p1: Double, mod: Double): Double {
        val a = p0 % mod
        val b = p1 % mod
        val d1 = Math.abs(b - a)
        val d2 = Math.abs(b - (a + mod))
        return min(d1, d2)
    }
}