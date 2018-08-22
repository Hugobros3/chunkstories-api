package io.xol.chunkstories.api.graphics

interface Texture {
    val format: TextureFormat
}

interface Texture1D : Texture {
    val width: Int
}

interface Texture2D : Texture {
    val width: Int
    val height: Int
}

interface Texture3D : Texture {
    val width: Int
    val height: Int
    val depth: Int
}

interface Cubemap : Texture {
    val width: Int
    val height: Int
}