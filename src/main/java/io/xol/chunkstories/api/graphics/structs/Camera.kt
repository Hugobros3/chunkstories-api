package io.xol.chunkstories.api.graphics.structs

import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import io.xol.chunkstories.api.physics.Frustrum
import org.joml.*

//TODO use double-precision and cast them when sending those to the GPU
data class Camera @JvmOverloads constructor(val position: Vector3fc = Vector3f(),
                                            val lookingAt: Vector3fc = Vector3f(0.0f, 0.0f, -1.0f),
                                            val up: Vector3fc = Vector3f(0.0f, 1.0f, 0.0f),
                                            val fov: Float = 90f,
                                            val viewMatrix: Matrix4fc = Matrix4f(),
                                            val projectionMatrix: Matrix4fc = Matrix4f()
) : InterfaceBlock {
    //@IgnoreGLSL
    //val frustrm = Frustrum(this)
}