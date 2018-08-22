package io.xol.chunkstories.api.graphics.representation

import io.xol.chunkstories.api.graphics.Texture2D

data class Surface(
        val albedo: Texture2D,
        val normal: Texture2D?,
        val metallic: Texture2D?,
        val roughness: Texture2D?,
        val ao: Texture2D?
)