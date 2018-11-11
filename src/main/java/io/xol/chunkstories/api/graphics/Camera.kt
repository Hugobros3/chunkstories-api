package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3fc

data class Camera(var viewMatrix: Matrix4f, var projectionMatrix: Matrix4f) : InterfaceBlock {
    constructor() : this(Matrix4f(), Matrix4f())
}