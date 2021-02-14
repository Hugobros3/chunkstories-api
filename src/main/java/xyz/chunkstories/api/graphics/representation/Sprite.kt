//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Vector3d
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.structs.IgnoreGLSL
import xyz.chunkstories.api.graphics.structs.InterfaceBlock

data class Sprite(val position: Vector3d, var size: Float, @IgnoreGLSL val material: MeshMaterial) : Representation, InterfaceBlock {
    constructor() : this(Vector3d(), 0f, MeshMaterial("", emptyMap(), ""))
}