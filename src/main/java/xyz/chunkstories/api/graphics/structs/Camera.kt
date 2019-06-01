//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.structs

import xyz.chunkstories.api.util.kotlin.getNormalMatrix
import xyz.chunkstories.api.util.kotlin.inverse
import org.joml.*
import xyz.chunkstories.api.client.Client
import xyz.chunkstories.api.graphics.GraphicsEngine
import xyz.chunkstories.api.physics.Frustrum
import xyz.chunkstories.api.util.kotlin.toVec3f
import java.lang.Math.floor

//TODO PerspectiveCamera & OrthogonalCamera
//TODO use double-precision and cast them when sending those to the GPU
@UpdateFrequency(frequency = UniformUpdateFrequency.ONCE_PER_RENDER_TASK)
data class Camera @JvmOverloads constructor(
        val position: Vector3dc = Vector3d(),
        val lookingAt: Vector3fc = Vector3f(0.0f, 0.0f, -1.0f),
        val up: Vector3fc = Vector3f(0.0f, 1.0f, 0.0f),
        val fov: Float = 90f,
        val viewMatrix: Matrix4fc = Matrix4f(),
        val projectionMatrix: Matrix4fc = Matrix4f(),
        val normalMatrix: Matrix3fc = viewMatrix.getNormalMatrix(),
        val viewMatrixInverted: Matrix4fc = viewMatrix.inverse(),
        val projectionMatrixInverted: Matrix4fc = projectionMatrix.inverse(),
        val normalMatrixInverted: Matrix3fc = normalMatrix.inverse()
    ) : InterfaceBlock {
    @IgnoreGLSL
    val frustrum = Frustrum(this)

    val combinedViewProjectionMatrix: Matrix4f
        get() { return Matrix4f().mul(projectionMatrix).mul(viewMatrix) }
}

fun Client.makeCamera(position: Vector3dc, direction: Vector3fc, up: Vector3fc, fov: Float): Camera {
    val fov = (fov / 360.0 * (Math.PI * 2)).toFloat()
    val aspect = gameWindow.width.toFloat() / gameWindow.height.toFloat()
    //val projectionMatrix = Matrix4f().perspective(fov, aspect, 2000f, 0.1f, true)
    val projectionMatrix = Matrix4f()
    val f = 1.0f / Math.tan(fov / 2.0).toFloat()
    val zNear = 0.1f

    projectionMatrix.set(f / aspect, 0.0f, 0.0f, 0.0f,
                            0.0f, f, 0.0f, 0.0f,
                            0.0f, 0.0f, 0.0f, -1.0f,
                            0.0f, 0.0f, zNear, 0.0f)

    //val cameraOffset = Vector3d(floor(position.x() / 32) * 32.0, floor(position.y() / 32) * 32.0, floor(position.z() / 32) * 32.0)
    //val cameraPosition = Vector3d(position).sub(cameraOffset).toVec3f()
    val cameraPosition = position.toVec3f()

    //cameraPosition.y += 1.8f

    val entityDirection = direction//(entity.traits[TraitRotation::class]?.directionLookingAt ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()
    val entityLookAt = Vector3f(cameraPosition).add(entityDirection)

    //val up = (entity.traits[TraitRotation::class]?.upDirection ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()

    val viewMatrix = Matrix4f()
    viewMatrix.lookAt(cameraPosition, entityLookAt, up)

    return Camera(position, entityDirection, up, fov, viewMatrix, projectionMatrix)
}