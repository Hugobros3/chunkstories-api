//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Vector3d
import org.joml.Vector3dc
import xyz.chunkstories.api.graphics.structs.InterfaceBlock

abstract class Light : Representation {
    abstract val position: Vector3dc
}

data class PointLight(override val position: Vector3dc = Vector3d(), var color: Vector3dc = Vector3d()) : Light(), InterfaceBlock

data class SpotLight(override val position: Vector3dc = Vector3d(), var color: Vector3dc = Vector3d(), var direction: Vector3dc = Vector3d()) : Light(), InterfaceBlock

data class DirectionalLight(override val position: Vector3dc = Vector3d(), var color: Vector3dc = Vector3d(), var direction: Vector3dc = Vector3d()) : Light(), InterfaceBlock