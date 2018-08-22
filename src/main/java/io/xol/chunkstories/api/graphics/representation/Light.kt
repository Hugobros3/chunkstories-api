package io.xol.chunkstories.api.graphics.representation

import org.joml.Vector3d

data class Light(var color : Vector3d) : RepresentationElement() {
    var enabled = true
}

data class SpotLight(var color : Vector3d, var direction : Vector3d) : RepresentationElement() {
    var enabled = true
}

data class DirectionalLight(var color : Vector3d, var direction : Vector3d) : RepresentationElement() {
    var enabled = true
}