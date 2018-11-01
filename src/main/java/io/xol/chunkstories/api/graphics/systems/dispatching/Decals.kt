package io.xol.chunkstories.api.graphics.systems.dispatching

import org.joml.Vector3dc

interface DecalsManager {
    fun add(position: Vector3dc, orientation: Vector3dc, size: Vector3dc, decalName: String)
}

interface DecalsRenderer : DecalsManager, DispatchingSystem {
}