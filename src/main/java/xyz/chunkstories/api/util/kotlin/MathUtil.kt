//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.kotlin

import org.joml.Matrix3f
import org.joml.Matrix3fc
import org.joml.Matrix4f
import org.joml.Matrix4fc
import java.util.*

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start

fun Matrix4fc.inverse() : Matrix4f {
    val inverse = Matrix4f()
    this.invert(inverse)
    return inverse
}

fun Matrix3fc.inverse() : Matrix3f {
    val inverse = Matrix3f()
    this.invert(inverse)
    return inverse
}

fun Matrix4fc.getNormalMatrix() : Matrix3f {
    val nm = Matrix3f()
    this.normal(nm)
    return nm
}