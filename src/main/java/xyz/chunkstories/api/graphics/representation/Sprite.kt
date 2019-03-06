package xyz.chunkstories.api.graphics.representation

import org.joml.Matrix4fc
import xyz.chunkstories.api.graphics.MeshMaterial

data class Sprite(val position: Matrix4fc, var size: Float, val material: MeshMaterial) : Representation