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
import xyz.chunkstories.api.client.IngameClient
import xyz.chunkstories.api.entity.traits.TraitCamera
import xyz.chunkstories.api.entity.traits.serializable.TraitRotation
import xyz.chunkstories.api.physics.Frustrum
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.PlayerState
import xyz.chunkstories.api.util.kotlin.toVec3f

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
}

fun Client.makeCamera(position: Vector3dc, direction: Vector3fc, up: Vector3fc, fov: Float): Camera {
    val fovRadiants = (fov / 360.0 * (Math.PI * 2)).toFloat()
    val aspect = gameWindow.width.toFloat() / gameWindow.height.toFloat()
    val projectionMatrix = Matrix4f()
    val f = 1.0f / Math.tan(fovRadiants / 2.0).toFloat()
    val zNear = 0.1f

    projectionMatrix.set(f / aspect, 0.0f, 0.0f, 0.0f,
                            0.0f, f, 0.0f, 0.0f,
                            0.0f, 0.0f, 0.0f, -1.0f,
                            0.0f, 0.0f, zNear, 0.0f)

    val cameraPosition = position.toVec3f()

    val entityLookAt = Vector3f(cameraPosition).add(direction)

    val viewMatrix = Matrix4f()
    viewMatrix.lookAt(cameraPosition, entityLookAt, up)

    return Camera(position, direction, up, fovRadiants, viewMatrix, projectionMatrix)
}

val IngameClient.camera: Camera
    get() {
        when(val state = player.state) {
            is PlayerState.Ingame -> {
                val entity = state.entity
                val traitCamera = entity.traits[TraitCamera::class]
                if (traitCamera != null)
                    return traitCamera.camera
                val entityDirection = (entity.traits[TraitRotation::class]?.directionLookingAt?.toVec3f() ?: Vector3f(0.0f, 0.0f, 1.0f))
                val up = (entity.traits[TraitRotation::class]?.upDirection?.toVec3f() ?: Vector3f(0.0f, 0.0f, 1.0f))
                return engine.makeCamera(entity.location, entityDirection, up, 90.0f)
            }
            is PlayerState.Spectating -> {
                // TODO handle spectating camera
            }
            else -> {}
        }
        return engine.makeCamera(world.properties.spawn, Vector3f(1f, 0f, 0f), Vector3f(0.0f, 0.0f, 1.0f), 90.0f)
    }
