//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.kotlin

import org.joml.*

// A bunch of helpful extension functions to JOML vectors

// 4D Vector casts
fun Vector4dc.toVec4f() = Vector4f(x().toFloat(), y().toFloat(), z().toFloat(), w().toFloat())
fun Vector4dc.toVec4i() = Vector4i(x().toInt()  , y().toInt()  , z().toInt()  , w().toInt())

fun Vector4fc.toVec4d() = Vector4d(x().toDouble(), y().toDouble(), z().toDouble(), w().toDouble())
fun Vector4fc.toVec4i() = Vector4i(x().toInt()   , y().toInt()   , z().toInt()   , w().toInt())

fun Vector4ic.toVec4f() = Vector4f(x().toFloat() , y().toFloat() , z().toFloat() , w().toFloat())
fun Vector4ic.toVec4d() = Vector4d(x().toDouble(), y().toDouble(), z().toDouble(), w().toDouble())

// 3D Vector casts
fun Vector3dc.toVec3f() = Vector3f(x().toFloat(), y().toFloat(), z().toFloat())
fun Vector3dc.toVec3i() = Vector3i(x().toInt(), y().toInt(), z().toInt())

fun Vector3fc.toVec3d() = Vector3d(x().toDouble(), y().toDouble(), z().toDouble())
fun Vector3fc.toVec3i() = Vector3i(x().toInt()   , y().toInt()   , z().toInt())

fun Vector3ic.toVec3f() = Vector3f(x().toFloat() , y().toFloat() , z().toFloat())
fun Vector3ic.toVec3d() = Vector3d(x().toDouble(), y().toDouble(), z().toDouble())

// 2D Vector casts
fun Vector2dc.toVec2f() = Vector2f(x().toFloat(), y().toFloat())
fun Vector2dc.toVec2i() = Vector2i(x().toInt(), y().toInt())

fun Vector2fc.toVec2d() = Vector2d(x().toDouble(), y().toDouble())
fun Vector2fc.toVec2i() = Vector2i(x().toInt()   , y().toInt())

fun Vector2ic.toVec2f() = Vector2f(x().toFloat() , y().toFloat())
fun Vector2ic.toVec2d() = Vector2d(x().toDouble(), y().toDouble())

// Downcasts
fun Vector4fc.toVec3f() = Vector3f(x(), y(), z())
fun Vector4fc.toVec2f() = Vector2f(x(), y())
fun Vector3fc.toVec2f() = Vector2f(x(), y())

fun Vector4dc.toVec3d() = Vector3d(x(), y(), z())
fun Vector4dc.toVec2d() = Vector2d(x(), y())
fun Vector3dc.toVec2d() = Vector2d(x(), y())

fun Vector4ic.toVec3i() = Vector3i(x(), y(), z())
fun Vector4ic.toVec2i() = Vector2i(x(), y())
fun Vector3ic.toVec2i() = Vector2i(x(), y())


fun Matrix4f.toMatrix4d() = Matrix4d(m00().toDouble(), m01().toDouble(), m02().toDouble(), m03().toDouble(),
        m10().toDouble(), m11().toDouble(), m12().toDouble(), m13().toDouble(),
        m20().toDouble(), m21().toDouble(), m22().toDouble(), m23().toDouble(),
        m30().toDouble(), m31().toDouble(), m32().toDouble(), m33().toDouble())

fun Matrix4d.toMatrix4f() = Matrix4f(m00().toFloat(), m01().toFloat(), m02().toFloat(), m03().toFloat(),
        m10().toFloat(), m11().toFloat(), m12().toFloat(), m13().toFloat(),
        m20().toFloat(), m21().toFloat(), m22().toFloat(), m23().toFloat(),
        m30().toFloat(), m31().toFloat(), m32().toFloat(), m33().toFloat())