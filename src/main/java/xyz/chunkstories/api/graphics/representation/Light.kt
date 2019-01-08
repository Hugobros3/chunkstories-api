//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Vector3d

data class Light(var color: Vector3d, override val parentObject: RepresentationElement?) : RepresentationElement(parentObject) {
    var enabled = true
}

data class SpotLight(var color: Vector3d, var direction: Vector3d, override val parentObject: RepresentationElement?) : RepresentationElement(parentObject) {
    var enabled = true
}

data class DirectionalLight(var color: Vector3d, var direction: Vector3d, override val parentObject: RepresentationElement?) : RepresentationElement(parentObject) {
    var enabled = true
}