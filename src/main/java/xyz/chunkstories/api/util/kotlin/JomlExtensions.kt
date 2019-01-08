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