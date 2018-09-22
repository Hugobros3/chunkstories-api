package io.xol.chunkstories.api.graphics.representation

open class Surface(val textures: Map<String, String>) {
    val albedo : String
        get() = textures.getOrDefault("albedo", "textures/notex.png")

    val normal : String
        get() = textures.getOrDefault("normal", "textures/normalnormal.png")

    //TODO other fast accessors ?
}