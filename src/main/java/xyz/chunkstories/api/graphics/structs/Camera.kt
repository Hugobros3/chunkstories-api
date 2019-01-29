//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.structs

import xyz.chunkstories.api.util.kotlin.getNormalMatrix
import xyz.chunkstories.api.util.kotlin.inverse
import org.joml.*

//TODO PerspectiveCamera & OrthogonalCamera
//TODO use double-precision and cast them when sending those to the GPU
@UpdateFrequency(frequency = UniformUpdateFrequency.ONCE_PER_RENDER_TASK)
data class Camera @JvmOverloads constructor(
        val position: Vector3fc = Vector3f(),
        val lookingAt: Vector3fc = Vector3f(0.0f, 0.0f, -1.0f),
        val up: Vector3fc = Vector3f(0.0f, 1.0f, 0.0f),
        val fov: Float = 90f,
        val viewMatrix: Matrix4fc = Matrix4f(),
        val projectionMatrix: Matrix4fc = Matrix4f(),
        val normalMatrix: Matrix3fc = viewMatrix.getNormalMatrix(),
        val viewMatrixInverted: Matrix4fc = viewMatrix.inverse(),
        val projectionMatrixInverted: Matrix4fc = projectionMatrix.inverse(),
        val normalMatrixInverted: Matrix3fc = normalMatrix.inverse()
) : InterfaceBlock