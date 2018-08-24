package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.graphics.representation.Representation

interface EntityRepresentation : Representation {
    val entity : Entity
}